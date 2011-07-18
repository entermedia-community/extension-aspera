importPackage( Packages.com.openedit.util );
importPackage( Packages.java.util );
importPackage( Packages.java.lang );
importPackage( Packages.com.openedit.modules.update );

var war = "http://dev.entermediasoftware.com/jenkins/job/entermedia-aspera/lastSuccessfulBuild/artifact/deploy/entermedia-aspera.zip";

var root = moduleManager.getBean("root").getAbsolutePath();
var web = root + "/WEB-INF";
var tmp = web + "/tmp";

log.add("1. GET THE LATEST WAR FILE");
var downloader = new Downloader();
downloader.download( war, tmp + "/entermedia-aspera.zip");

log.add("2. UNZIP WAR FILE");
var unziper = new ZipUtil();
unziper.unzip(  tmp + "/entermedia-aspera.zip",  tmp );

log.add("3. REPLACE LIBS");
var files = new FileUtils();
files.deleteMatch( web + "/lib/entermedia-aspera*.jar");
files.deleteMatch( web + "/lib/aspera*.jar");

files.copyFileByMatch( tmp + "/lib/aspera*.jar", web + "/lib/");
files.copyFileByMatch( tmp + "/lib/entermedia-aspera*.jar", web + "/lib/");



files.deleteMatch( web + "/bin/windows/aspera/")
files.copyFileByMatch( tmp + "/bin/windows/aspera", web + "/bin/windows/aspera/");

files.deleteMatch( web + "/bin/linux/aspera/")
files.copyFileByMatch( tmp + "/bin/linux/aspera", web + "/bin/linux/aspera/");



log.add("5. CLEAN UP");
files.deleteAll(tmp);

log.add("6. UPGRADE COMPLETED");