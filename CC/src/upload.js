const util = require("util");
const Multer = require("multer");


let processImage = Multer({
  storage: Multer.memoryStorage(),
}).single("file");


let processFileMiddleware = util.promisify(processImage);
module.exports = processFileMiddleware;