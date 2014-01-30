//
//  CDVZipper.m
//
//
//  Created by Gianluca Pisati on 21/01/14.
//
//

#import "CDVZipper.h"

@implementation CDVZipper

- (void)zip:(CDVInvokedUrlCommand *)command {
    // Retrieve the JavaScript-created date String from the CDVInvokedUrlCommand instance.
    // When we implement the JavaScript caller to this function, we'll see how we'll
    // pass an array (command.arguments), which will contain a single String.
    NSString *folderToZip       = [command.arguments objectAtIndex:0];
    
    [self zipFolder:folderToZip];
    
    // Create an object with a simple success property.
    NSDictionary *jsonObj = [[NSDictionary alloc] initWithObjectsAndKeys : @"true", @"success", nil];
    
    CDVPluginResult *pluginResult = [ CDVPluginResult
                                     resultWithStatus    : CDVCommandStatus_OK
                                     messageAsDictionary : jsonObj
                                     ];
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}



- (void)zipFolder:(NSString*)folderToZip{
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0]; // Get documents folder
    NSString *dataPath = [documentsDirectory stringByAppendingPathComponent:[NSString stringWithFormat:@"/%@",folderToZip]];
    
    BOOL isDir=NO;
    NSArray *subpaths;
    NSString *exportPath = documentsDirectory;
    NSFileManager *fileManager = [NSFileManager defaultManager];
    if ([fileManager fileExistsAtPath:dataPath isDirectory:&isDir] && isDir){
        subpaths = [fileManager subpathsAtPath:dataPath];
    }
    
    NSString *archivePath = [dataPath stringByAppendingString:@".zip"];
    
    ZipArchive *archiver = [[ZipArchive alloc] init];
    [archiver CreateZipFile2:archivePath];
    for(NSString *path in subpaths)
    {
        NSString *longPath = [exportPath stringByAppendingPathComponent:path];
        if([fileManager fileExistsAtPath:longPath isDirectory:&isDir] && !isDir)
        {
            [archiver addFileToZip:longPath newname:path];
        }
    }
    BOOL successCompressing = [archiver CloseZipFile2];
    if(successCompressing)
        NSLog(@"Success");
    else
        NSLog(@"Fail");
}

@end
