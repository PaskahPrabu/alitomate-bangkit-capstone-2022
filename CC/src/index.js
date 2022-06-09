const express = require("express");
const router = express.Router();
const controller = require("./file.controller");

let routes = (app) => {
  router.post("/upload", controller.upload );
  router.post("/report", controller.filereport);
  //router.get("/files", controller.getimages);
  app.use(router);
};

module.exports = routes;