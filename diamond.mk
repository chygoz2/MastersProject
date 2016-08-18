JFLAGS  = -g
JC = javac
JVM = java
SHELL := /bin/bash
INSTANCES := $(shell cut -d' ' -f1 instances.txt)
RESULTSDIR := results
GRAPHDIR := generated_graphs

diamond: $(foreach i, $(INSTANCES), $(RESULTSDIR)/efficient_diamond_detection_result/$(i))

$(RESULTSDIR)/efficient_diamond_detection_result/%:
	mkdir -p $(RESULTSDIR)/efficient_diamond_detection_result
	$(JVM) efficientdetection/DetectDiamond $(GRAPHDIR)/$* | tee $(RESULTSDIR)/efficient_diamond_detection_result/$*
