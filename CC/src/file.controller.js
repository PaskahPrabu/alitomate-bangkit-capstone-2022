const processImage = require("./upload");
const { format } = require("util");
const { Storage } = require("@google-cloud/storage");
const bodyParser = require('body-parser');
const admin = require("firebase-admin");
const serviceAccount = require("../firebase-key.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});
const database = admin.firestore()

const storage = new Storage({ keyFilename: "storage-key.json" });
const bucket = storage.bucket("alitemate-storage");

const upload = async (req, res) => {
  try {
    await processImage(req, res);

    const blob = bucket.file(req.file.originalname);
    const blobStream = blob.createWriteStream({
      resumable: false,
    });

    blobStream.on("error", (err) => {
      res.status(500).send({ message: err.message });
    });

    blobStream.on("finish", async (data) => {
      const publicUrl = format(
        `https://storage.googleapis.com/${bucket.name}/${blob.name}`
      );

      try {
        await bucket.file(req.file.originalname).makePublic();
      } catch {
        return res.status(200).send({
          message:
            `Foto berhasil terkirim' ${req.file.originalname}`,
          url: publicUrl,
        });
      }
    });

    blobStream.end(req.file.buffer);
  } catch (err) {
    res.status(500).send({
      message: `Foto tidak terkirim ${req.file.originalname}. ${err}`,
    });
  }
};

const getimages = async (req, res) => {
  try {
    const [files] = await bucket.getFiles();
    let imageInfo = [];

    files.forEach((file) => {
      imageInfo.push({
        name: file.name,
        url: file.metadata.mediaLink,
      });
    });

    res.status(200).send(imageInfo);
  } catch (err) {
    console.log(err);

    res.status(500).send({
      message: "error",
    });
  }
};


const filereport = async(req, res) => {
  try {
    
    console.log(req.body);
    const id = req.body.nama;
    
    var userreport = {
    nama : req.body.nama,
    };
  
    await database.collection("reports").doc(id).set(req.body);
    res.status(200).send({
      userreport
    });
  } catch(error){
    res.status(400).send({message: "Laporan tidak terkirim"});
  }
 }

module.exports = {
  upload,
  getimages,
  filereport,
}