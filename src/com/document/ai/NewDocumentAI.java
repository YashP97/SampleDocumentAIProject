package com.document.ai;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import com.google.cloud.documentai.v1.DocumentProcessorServiceClient;
import com.google.cloud.documentai.v1.DocumentProcessorServiceSettings;
import com.google.cloud.documentai.v1.ProcessRequest;
import com.google.cloud.documentai.v1.ProcessResponse;
import com.google.cloud.documentai.v1.RawDocument;
import com.google.protobuf.ByteString;

public class NewDocumentAI {

	public static void main(String[] args)
			throws IOException, InterruptedException, ExecutionException, TimeoutException {
		String projectId = "newdocumentsaiproject";     //"documentai-416406";
		String location = "us"; // Format is "us" or "eu".
		String processorId = "44b255307df5a198";     //"16c7da0c81f9b897"
	    String filePath = "path/to/input/file.pdf";
	    quickStart(projectId, location, processorId, filePath);

	}

	public static void quickStart(String projectId, String location, String processorId, String filePath)
			throws IOException, InterruptedException, ExecutionException, TimeoutException {
		// Initialize client that will be used to send requests. This client only needs
		// to be created
		// once, and can be reused for multiple requests. After completing all of your
		// requests, call
		// the "close" method on the client to safely clean up any remaining background
		// resources.
		String endpoint = String.format("%s-documentai.googleapis.com:443", location);
		DocumentProcessorServiceSettings settings = DocumentProcessorServiceSettings.newBuilder().setEndpoint(endpoint)
				.build();
		try (DocumentProcessorServiceClient client = DocumentProcessorServiceClient.create(settings)) {
			// The full resource name of the processor, e.g.:
			// projects/project-id/locations/location/processor/processor-id
			// You must create new processors in the Cloud Console first
			String name = String.format("projects/%s/locations/%s/processors/%s", projectId, location, processorId);

			// Read the file.
			byte[] imageFileData = Files.readAllBytes(Paths.get(filePath));

			// Convert the image data to a Buffer and base64 encode it.
			ByteString content = ByteString.copyFrom(imageFileData);

			RawDocument document = RawDocument.newBuilder().setContent(content).setMimeType("application/pdf").build();

			// Configure the process request.
			ProcessRequest request = ProcessRequest.newBuilder().setName(name).setRawDocument(document).build();

			// Recognizes text entities in the PDF document
			ProcessResponse result = client.processDocument(request);
			Document documentResponse = result.getDocument();

			// Get all of the document text as one big string
			String text = documentResponse.getText();

			// Read the text recognition output from the processor
			System.out.println("The document contains the following paragraphs:");
			Document.Page firstPage = documentResponse.getPages(0);
			List<Document.Page.Paragraph> paragraphs = firstPage.getParagraphsList();

			for (Document.Page.Paragraph paragraph : paragraphs) {
				String paragraphText = getText(paragraph.getLayout().getTextAnchor(), text);
				System.out.printf("Paragraph text:\n%s\n", paragraphText);
			}
		}
	}

	// Extract shards from the text field
	private static String getText(Document.TextAnchor textAnchor, String text) {
		if (textAnchor.getTextSegmentsList().size() > 0) {
			int startIdx = (int) textAnchor.getTextSegments(0).getStartIndex();
			int endIdx = (int) textAnchor.getTextSegments(0).getEndIndex();
			return text.substring(startIdx, endIdx);
		}
		return "[NO TEXT]";
	}

}
