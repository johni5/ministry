#!/bin/sh

mvn install:install-file -Dfile=jasperreports-additional-fonts.jar -DgeneratePom=true -DgroupId=jasperreports-additional-fonts -DartifactId=jasperreports-additional-fonts -Dversion=1 -Dpackaging=jar