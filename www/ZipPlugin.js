var extractzip = {
	unzip: function(file, success, fail){
		cordova.exec(success, fail, "ExtractZipFilePlugin", "unzip", file);
	}
}

module.exports = extractzip;