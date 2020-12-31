JDK_HOME := /opt/openjdk7
JDK_JAVAC = $(JDK_HOME)/bin/javac
JDK_JAVAC_FLAGS = -source 1.6 -target 1.6 -implicit:none
#JAR = $(JDK_HOME)/bin/jar
7Z ?= 7za
CLASS_PATH = ../java-config/config.jar
BUKKIT_CLASS_PATH = /opt/minecraft/cauldron-1.7.10-1.1206.01.175-server.jar

BUKKIT_PLUGIN_CLASSES := \
	build/rivoreo/minecraft/plugin/boomarrow/BukkitPlugin.class

default:	build boom-arrow-bukkit.jar

boom-arrow-bukkit.jar:	build/META-INF/MANIFEST.MF build/COPYING $(BUKKIT_PLUGIN_CLASSES) build/plugin.yml
	rm -f $@
	cd build && $(7Z) a -tzip ../$@ $(patsubst build/%,%,$(subst $$,\$$,$^))

clean:
	rm -f boom-arrow-bukkit.jar build/META-INF/MANIFEST.MF build/COPYING $(BUKKIT_PLUGIN_CLASSES) build/mcmod.info build/plugin.yml

build:
	mkdir $@

build/rivoreo/minecraft/plugin/boomarrow/Bukkit%.class:	src/main/java/rivoreo/minecraft/plugin/boomarrow/Bukkit%.java
	$(JDK_JAVAC) $(JDK_JAVAC_FLAGS) -classpath $(BUKKIT_CLASS_PATH):$(CLASS_PATH) -d build $<

build/%.class:	src/main/java/%.java
	$(JDK_JAVAC) $(JDK_JAVAC_FLAGS) -d build $<

build/%$$*.class:	src/main/java/%.java

build/%.yml:	src/main/resources/%.yml
	cp -p $< $@

build/META-INF/MANIFEST.MF:
	[ -d build/META-INF ] || mkdir -p build/META-INF
	echo "Class-Path: libs/rivoreo.config.jar" > $@

build/COPYING:	COPYING
	cp -p $< $@
