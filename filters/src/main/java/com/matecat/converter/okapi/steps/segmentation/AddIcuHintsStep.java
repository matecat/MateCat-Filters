package com.matecat.converter.okapi.steps.segmentation;

import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.util.ULocale;
import net.sf.okapi.common.Event;
import net.sf.okapi.common.pipeline.BasePipelineStep;
import net.sf.okapi.common.resource.ITextUnit;
import net.sf.okapi.common.resource.Segment;
import net.sf.okapi.common.resource.TextContainer;
import net.sf.okapi.common.resource.TextFragment;

import java.util.Locale;

public class AddIcuHintsStep extends BasePipelineStep {

    public final static char SENTENCE_BOUNDARY_PLACEHOLDER = '\uE105';
    private final static String UNICODE_WHITESPACES = "[\\pZ\\pC]";
    private final BreakIterator sentenceIterator;

    public AddIcuHintsStep(Locale sourceLocale) {
        super();
        this.sentenceIterator = BreakIterator.getSentenceInstance(ULocale.forLocale(sourceLocale));
    }

    @Override
    protected Event handleTextUnit (Event event) {
        final ITextUnit tu = event.getTextUnit();
        if (tu.isTranslatable()) {
            final TextContainer tc = tu.getSource();
            for (Segment seg : tc.getSegments()) {
                final TextFragment textFragment = seg.getContent();
                // Convert coded text to char array for faster direct access to chunks
                final char[] codedText = textFragment.getCodedText().toCharArray();
                // The sentence iterator will be executed on a clean version of the
                // text, without codes. This because codes chars can interfere with
                // the ICU sentence boundary detection.
                final String cleanText = textFragment.getText()
                        // I also transform all strange whitespaces in normal ones, to easily
                        // trim them out. This covers also newlines: ICU breaks on newlines,
                        // but we don't want this, so we treat newlines as normal whitespaces.
                        .replaceAll(UNICODE_WHITESPACES, " ")
                        // Finally I replace all the "horizontal ellipsis" with simple periods,
                        // because ICU is not breaking on them, but it should.
                        .replace('\u2026', '.');
                sentenceIterator.setText(cleanText);
                // This will contain the final segment with marked sentences boundaries
                final StringBuilder codedTextWithHints = new StringBuilder();
                // Vars to keep track of analyzed chars
                int prevCodedTextIndex = 0;
                int curCodedTextIndex = 0;
                int curCleanTextIndex = 0;

                for (int boundary = sentenceIterator.next(); boundary != BreakIterator.DONE; boundary = sentenceIterator.next()) {
                    // ICU puts always a boundary at the end of the string: skip it
                    if (boundary == cleanText.length()) continue;
                    // Boundary is "the zero-based index of the character following the boundary"
                    // (see http://userguide.icu-project.org/boundaryanalysis)
                    // Moreover, if there are lots of spaces between a sentence and the following,
                    // ICU puts the boundary after all the spaces, just before the first char of
                    // the next sentence. We want the boundary before these spaces, and so this loop.
                    while (cleanText.charAt(boundary - 1) == ' ' && boundary > 0) {
                        boundary--;
                    }

                    // We found a sentence boundary in the text without codes, lets
                    // obtain the index in the string with the codes.
                    // We start counting chars in the coded text, skipping the codes;
                    // when the count reaches the index of the boundary, we know the
                    // corresponding chunk in the coded text.
                    while (curCleanTextIndex < boundary) {
                        if (TextFragment.isMarker(codedText[curCodedTextIndex])) {
                            // A code in the coded string is composed by a "marker char"
                            // and another following char representing a counter: skip
                            // them both
                            curCodedTextIndex += 2;
                        } else {
                            // We are on a regular char, go one character forward in both strings
                            curCleanTextIndex++;
                            curCodedTextIndex++;
                        }
                    }

                    // Ok, lets add the sentence to the output with the special delimiter
                    codedTextWithHints
                            .append(codedText, prevCodedTextIndex, curCodedTextIndex - prevCodedTextIndex)
                            .append(SENTENCE_BOUNDARY_PLACEHOLDER);

                    // Remember the end of this sentence
                    prevCodedTextIndex = curCodedTextIndex;
                }

                // Add the remaining text from the last sentence boundary to the end of the string
                codedTextWithHints
                        .append(codedText, curCodedTextIndex, codedText.length - curCodedTextIndex);
                // Replace the segment with the new augmented one
                textFragment.setCodedText(codedTextWithHints.toString());
            }
        }
        return event;
    }

    @Override
    public String getName() {
        return "AddIcuHintsStep";
    }

    @Override
    public String getDescription() {
        return "Processes all the segments with the ICU sentence boundary iterator, and adds a special character where the ICU thinks there's a boundary. These special characters can be used in SRX rules to improve segmentation. After the regular SRX segmentation step, the special characters MUST be removed from segments using the RemoveIcuHintsStep.";
    }
}