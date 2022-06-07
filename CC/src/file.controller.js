const processFile = require("./upload");
const { format } = require("util");
const { Storage } = require("@google-cloud/storage");
const bodyParser = require('body-parser');


const admin = require("firebase-admin");
const credentials = require("../firebase-key.json");
const { app } = require("firebase-admin");
admin.initializeApp({
  credentials: admin.credential.cert(credentials)
});
const db = admin.firestore()

const storage = new Storage({ keyFilename: "storage-key.json" });
const bucket = storage.bucket("c22-pc378");

const upload = async (req, res) => {
  try {
    await processFile(req, res);

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

const getListFiles = async (req, res) => {
  try {
    const [files] = await bucket.getFiles();
    let fileInfos = [];

    files.forEach((file) => {
      fileInfos.push({
        name: file.name,
        url: file.metadata.mediaLink,
      });
    });

    res.status(200).send(fileInfos);
  } catch (err) {
    console.log(err);

    res.status(500).send({
      message: "Unable to read list of files!",
    });
  }
};


const filereport = async(req, res) => {
  try {
    
    console.log(req.body);
    const id = req.body.nama;
    
    var userreport = {
    nama : req.body.nama,
    nomor_telepon : req.body.nomor_telepon,
    detail_lokasi : req.body.detail_lokasi,
    keterangan_kebakaran : req.body.keterangan_kebakaran,
    location : req.body.location
    };
  
    const response = await db.collection("reports").doc(id).set(userreport);
    res.status(200).send({message: "Laporan terkirim"});
  } catch(error){
    res.status(400).send({message: "Laporan tidak terkirim"});
  }
 }

module.exports = {
  upload,
  getListFiles,
  filereport,
}