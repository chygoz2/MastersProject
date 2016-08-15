JFLAGS  = -g
JC = javac
JVM = java
SHELL := /bin/bash
INSTANCES := $(shell cut -d' ' -f1 instances.txt)
RESULTSDIR := results
PATTERNDIR := efficient_claw_detection_result
PATTERNALG := efficientdetection/DetectClaw

default: $(foreach i, $(INSTANCES), $(RESULTS)/$(PATTERN)/$(i))

$(RESULTSDIR)/$(PATTERNDIR)/%:
	mkdir -p $(RESULTSDIR)$(PATTERNDIR)
	$(JVM) efficientdetection $(PATTERNALG) $* | tee $(RESULTSDIR)/$(PATTERNDIR)/$*
