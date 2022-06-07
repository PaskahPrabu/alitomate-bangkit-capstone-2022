const cors = require("cors");
const express = require("express");
const app = express();
const bodyParser = require('body-parser');


let corsOptions = {
  origin: "http://localhost:8081",
};

app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(cors(corsOptions));
app.use(bodyParser.json());


const initRoutes = require("./src");
const { exp } = require("@tensorflow/tfjs");


initRoutes(app);

const port = 8080;
app.listen(port, () => {
  console.log(`Running at localhost:${port}`);
});