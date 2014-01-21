var extractzip = {
	unzip: function(file, success, fail){
		cordova.exec(success, fail, "CDVUnzipper", "unzip", file);
	}
}

module.exports = extractzip;