# De Bijenkorf Assigment

## Executing program
Before running the application you need to add S3 bucket name, region, access key, and access secret to the application.yml file.\
`aws`:\
`s3-endpoint`: `bucket-name`\
`s3-region`:\
`access-key`:\
`secret-key`:\

## Notes
downloadImageFromOriginalSource method is mocked. It just returns an image(arandompicture.jpg) from images folder in the resources.\
\
optimizeImage method is mocked. It also returns an image(arandompicture_optimized.jpg) from images folder in the resources instead of optimizing image according to predefined properties.
