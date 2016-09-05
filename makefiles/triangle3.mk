JFLAGS = -g
JC = javac
JVM = java
SHELL := /bin/bash
INSTANCES := $(shell cut -d' ' -f1 listInstances.txt)
RESULTSDIR := results
GRAPHDIR := generated_graphs

triangle3: $(foreach i, $(INSTANCES), $(RESULTSDIR)/efficient_triangle_listing_result/$(i))

$(RESULTSDIR)/efficient_triangle_listing_result/%:
	mkdir -p $(RESULTSDIR)/efficient_triangle_listing_result
	$(JVM) efficient.listing.ListTriangles $(GRAPHDIR)/$* | tee $(RESULTSDIR)/efficient_triangle_listing_result/$*
