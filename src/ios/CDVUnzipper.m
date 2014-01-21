//
//  CDVUnzipper.m
//  
//
//  Created by Gianluca Pisati on 21/01/14.
//
//

#import "CDVUnzipper.h"

@implementation CDVUnzipper
    
- (void)unzip:(CDVInvokedUrlCommand *)command {
    // Retrieve the JavaScript-created date String from the CDVInvokedUrlCommand instance.
    // When we implement the JavaScript caller to this function, we'll see how we'll
    // pass an array (command.arguments), which will contain a single String.
    NSString *zipFile           = [command.arguments objectAtIndex:0];
    NSString *destinationFolder = [command.arguments objectAtIndex:1];
    
    [self unzipFile:zipFile toFolder:destinationFolder];
    
    // Create an object with a simple success property.
    NSDictionary *jsonObj = [[NSDictionary alloc] initWithObjectsAndKeys : @"true", @"success", nil];
    
    CDVPluginResult *pluginResult = [ CDVPluginResult
                                     resultWithStatus    : CDVCommandStatus_OK
                                     messageAsDictionary : jsonObj
                                     ];
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

    
    
- (void)unzipFile:(NSString*)zipFile toFolder:(NSString*)destinationFolder;
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0]; // Get documents folder
    NSString *dataPath = [documentsDirectory stringByAppendingPathComponent:[NSString stringWithFormat:@"/%@",destinationFolder]];
    
    if (![[NSFileManager defaultManager] fileExistsAtPath:dataPath])
        [[NSFileManager defaultManager] createDirectoryAtPath:dataPath withIntermediateDirectories:NO attributes:nil error:&error];
    
    ZipArchive* za = [[ZipArchive alloc] init];
    
    if([za UnzipOpenFile:zipFile){
        BOOL ret = [za UnzipFileTo:dataPath overWrite:YES];
        if( NO==ret )
        {
            // error handler here
        }else{
            // success handler here
        }
        [za UnzipCloseFile];
    }
}
    
@end
