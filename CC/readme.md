# About projeck 
- Project name : alitemate
- Project number: 780960932153 
- Project ID: alitemate-352904 
- CREDENTIALS : alitemate-352904-4192f52c182b.json

 
# Cloud storage bucket model machine learning 
- **Public URL** 
```
https://storage.googleapis.com/model-alitemate-h5/fire_detection_test1_alitomate.h5
```
- **Authenticated URL** 
```
https://storage.cloud.google.com/model-alitemate-h5/fire_detection_test1_alitomate.h5
```
- **gsutil URI /MODEL_PATH**
```
gs://model-alitemate-h5/fire_detection_test1_alitomate.h5 
```

# alitomate-API

## Add Report :
- **Endpoint :** ```/report```
- **Method : POST**
- Body :  
```
{
  "nama" : "",
  "nomor_telepon" : "",
  "detail_lokasi" : "",
  "keterangan_kebakaran" : "",
  "location" : ""
  
}
```
- Response : 
```
{
  "userreport": {
  "nama" : "",
  "nomor_telepon" : "",
  "detail_lokasi" : "",
  "keterangan_kebakaran" : "",
  "location" : ""
  }
}
```

## Upload Image
- **Endpoint :** ```/upload```
- **Method : POST**
- Body :
```
{
  "file" : ""
}
```
- Response : 
```
{
  "message": "",
    "url":
}
```


## reverensi
- [How to load Keras h5 model format from Google Cloud Bucket] (https://medium.com/analytics-vidhya/how-to-load-keras-h5-model-format-from-google-cloud-bucket-abf9a77d3cb4)



