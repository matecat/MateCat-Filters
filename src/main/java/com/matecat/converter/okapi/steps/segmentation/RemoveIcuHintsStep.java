package com.matecat.converter.okapi.steps.segmentation;

import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.util.ULocale;
import net.sf.okapi.common.Event;
import net.sf.okapi.common.LocaleId;
import net.sf.okapi.common.pipeline.BasePipelineStep;
import net.sf.okapi.common.resource.ITextUnit;
import net.sf.okapi.common.resource.Segment;
import net.sf.okapi.common.resource.TextContainer;
import net.sf.okapi.common.resource.TextFragment;

public class RemoveIcuHintsStep extends BasePipelineStep {

    @Override
    protected Event handleTextUnit (Event event) {
        ITextUnit textUnit = event.getTextUnit();
        TextContainer textContainer;
        if ( textUnit.isTranslatable() ) {
            textContainer = textUnit.getSource();
            for ( Segment seg : textContainer.getSegments() ) {
                removeSentenceBoundariesPlaceholders(seg);
            }

            for (LocaleId localeId : textUnit.getTargetLocales()) {
                textContainer = textUnit.getTarget(localeId);
                for ( Segment seg : textContainer.getSegments() ) {
                    removeSentenceBoundariesPlaceholders(seg);
                }
            }
        }
        return event;
    }

    private void removeSentenceBoundariesPlaceholders(Segment segment) {
        TextFragment tf = segment.getContent();
        tf.setCodedText(tf.getCodedText().replace("" + AddIcuHintsStep.SENTENCE_BOUNDARY_PLACEHOLDER, ""));
    }

    @Override
    public String getName() {
        return "RemoveIcuHintsStep";
    }

    @Override
    public String getDescription() {
        return "Removes the special characters added by the AddIcuHintsStep, restoring the original segments.";
    }
}