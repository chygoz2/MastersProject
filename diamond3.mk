JFLAGS = -g
JC = javac
JVM = java
SHELL := /bin/bash
INSTANCES := $(shell cut -d' ' -f1 trimmedListInstances.txt)
RESULTSDIR := results
GRAPHDIR := generated_graphs

diamond3: $(foreach i, $(INSTANCES), $(RESULTSDIR)/efficient_diamond_listing_result/$(i))

$(RESULTSDIR)/efficient_diamond_listing_result/%:
	mkdir -p $(RESULTSDIR)/efficient_diamond_listing_result
	$(JVM) efficient.listing.ListDiamonds $(GRAPHDIR)/$* | tee $(RESULTSDIR)/efficient_diamond_listing_result/$*
