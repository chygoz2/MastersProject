JFLAGS  = -g
JC = javac
JVM = java
SHELL := /bin/bash
INSTANCES := $(shell cut -d' ' -f1 instances.txt)
RESULTSDIR := results
GRAPHDIR := generated_graphs

claw: $(foreach i, $(INSTANCES), $(RESULTSDIR)/efficient_claw_detection_result/$(i))

$(RESULTSDIR)/efficient_claw_detection_result/%:
	mkdir -p $(RESULTSDIR)/efficient_claw_detection_result
	$(JVM) efficientdetection/DetectClaw $(GRAPHDIR)/$* | tee $(RESULTSDIR)/efficient_claw_detection_result/$*
