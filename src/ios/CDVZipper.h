//
//  CDVZipper.h
//  
//
//  Created by Gianluca Pisati on 30/01/14.
//
//

#import <Cordova/CDV.h>
#import "ZipArchive.h"

@interface CDVZipper : CDVPlugin

- (void) zip:(CDVInvokedUrlCommand *)command;

#pragma mark - Util_Methods
- (void)zipFolder:(NSString*)folderToZip;


@end
