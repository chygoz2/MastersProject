JFLAGS  = -g
JC = javac
JVM = java
SHELL := /bin/bash
INSTANCES := $(shell cut -d' ' -f1 instances.txt)
RESULTSDIR := results
GRAPHDIR := generated_graphs

claw: $(foreach i, $(INSTANCES), $(RESULTSDIR)/bruteforce_claw_detection_result/$(i))

$(RESULTSDIR)/bruteforce_claw_detection_result/%:
	mkdir -p $(RESULTSDIR)/bruteforce_claw_detection_result
	$(JVM) bruteforce.detection.DetectClaw $(GRAPHDIR)/$* | tee $(RESULTSDIR)/bruteforce_claw_detection_result/$*
