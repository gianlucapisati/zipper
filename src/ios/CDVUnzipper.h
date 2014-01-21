//
//  CDVUnzipper.h
//  
//
//  Created by Gianluca Pisati on 21/01/14.
//
//

#import <Cordova/CDV.h>
#import "ZipArchive/ZipArchive.h"


@interface CDVUnzipper : CDVPlugin

- (void) unzip:(CDVInvokedUrlCommand *)command;

#pragma mark - Util_Methods
    - (void) unzipFile:(NSString*)zipFile toFolder:(NSString*)destinationFolder;

    
@end
