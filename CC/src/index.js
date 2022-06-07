const express = require("express");
const router = express.Router();
const controller = require("./file.controller");

let routes = (app) => {
  router.post("/upload", controller.upload );
  //router.get("/files", controller.getfilesreport);
  router.post("/report", controller.filereport);
  app.use(router);
};

module.exports = routes;