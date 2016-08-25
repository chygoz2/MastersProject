JFLAGS  = -g
JC = javac
JVM = java
SHELL := /bin/bash
INSTANCES := $(shell cut -d' ' -f1 trimmedinstances.txt)
RESULTSDIR := results
GRAPHDIR := generated_graphs

claw: $(foreach i, $(INSTANCES), $(RESULTSDIR)/efficient_claw_detection_result/$(i))

$(RESULTSDIR)/efficient_claw_detection_result/%:
	mkdir -p $(RESULTSDIR)/efficient_claw_detection_result
	$(JVM) efficient.detection.DetectClaw $(GRAPHDIR)/$* | tee $(RESULTSDIR)/efficient_claw_detection_result/$*
