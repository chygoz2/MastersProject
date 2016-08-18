JFLAGS  = -g
JC = javac
JVM = java
SHELL := /bin/bash
INSTANCES := $(shell cut -d' ' -f1 instances.txt)
RESULTSDIR := results
GRAPHDIR := generated_graphs

diamond: $(foreach i, $(INSTANCES), $(RESULTSDIR)/bruteforce_diamond_detection_result/$(i))

$(RESULTSDIR)/bruteforce_diamond_detection_result/%:
	mkdir -p $(RESULTSDIR)/bruteforce_diamond_detection_result
	$(JVM) easydetection/DetectDiamond $(GRAPHDIR)/$* | tee $(RESULTSDIR)/bruteforce_diamond_detection_result/$*
