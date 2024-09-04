# SampleDocumentAIProject
This project gives overview about how to integrate google cloud document ai processor to our project. From this project, user can use their invoice parser to parse invoices and it categories data with its fields from invoice.

Pre-requisites : 
a) Java
b) Maven
c) Account on Google Cloud Console and a project with service-account with enabled DocumentAI API and activated processor for classifying documents.
d) Download a file with json extension. Create it from your processor. It will contain all your credentials and data.
e) Set this .json file to your system variables with the name of GOOGLE_APPLICATION_CREDENTIALS. This will help the code to take all your credentials from your system.

For more information, check DocumentAI documentation.

Add your projectId, processorId and location accordingly as on google cloud console. You have to give filepath of your local file.

Project is now ready to use. Extend it according to your requirements.
