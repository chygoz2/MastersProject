JFLAGS  = -g
JC = javac
JVM = java
SHELL := /bin/bash
INSTANCES := $(shell cut -d' ' -f1 instances.txt)
RESULTSDIR := results
GRAPHDIR := generated_graphs

simplicial: $(foreach i, $(INSTANCES), $(RESULTSDIR)/bruteforce_simplicial_detection_result/$(i))

$(RESULTSDIR)/bruteforce_simplicial_detection_result/%:
	mkdir -p $(RESULTSDIR)/bruteforce_simplicial_detection_result
	$(JVM) bruteforce.detection.DetectSimplicialVertex $(GRAPHDIR)/$* | tee $(RESULTSDIR)/bruteforce_simplicial_detection_result/$*
