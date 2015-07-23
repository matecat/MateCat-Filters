package com.matecat.converter.core.okapiclient;

import com.matecat.converter.core.format.Format;
import net.sf.okapi.common.filters.IFilter;
import net.sf.okapi.filters.html.HtmlFilter;
import net.sf.okapi.filters.idml.IDMLFilter;
import net.sf.okapi.filters.json.JSONFilter;
import net.sf.okapi.filters.mif.MIFFilter;
import net.sf.okapi.filters.openoffice.OpenOfficeFilter;
import net.sf.okapi.filters.openxml.ConditionalParameters;
import net.sf.okapi.filters.openxml.OpenXMLFilter;
import net.sf.okapi.filters.php.PHPContentFilter;
import net.sf.okapi.filters.plaintext.PlainTextFilter;
import net.sf.okapi.filters.po.POFilter;
import net.sf.okapi.filters.properties.PropertiesFilter;
import net.sf.okapi.filters.railsyaml.RailsYamlFilter;
import net.sf.okapi.filters.rainbowkit.RainbowKitFilter;
import net.sf.okapi.filters.txml.Parameters;
import net.sf.okapi.filters.txml.TXMLFilter;
import net.sf.okapi.filters.xliff.XLIFFFilter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Reneses on 7/22/15.
 */
public class OkapiFilterFactory {

    // Formats supported by the filter factory
    protected static Set<Format> supportedFormats;
    static {
        supportedFormats = new HashSet<>(Arrays.asList(
                Format.DOCX,
                Format.PPTX,
                Format.XLSX,
                Format.TXT,
                Format.HTML,
                Format.XHTML,
                Format.HTM,
                Format.ODP,
                Format.ODS,
                Format.ODT,
                Format.PHP,
                Format.PROPERTIES,
                Format.PO,
                Format.XLF,
                Format.XLIFF,
                Format.JSON,
                Format.IDML,
                Format.TXML,
                Format.YML,
                Format.RKM,
                Format.MIF));
    }

    protected static boolean isSupported(Format format) {
        return supportedFormats.contains(format);
    }


    protected static IFilter getFilter(Format format) {

        // Get the corresponding filter depending on the format
        switch (format) {

            case DOCX:
            case PPTX:
            case XLSX:
                return getOpenXMLFilter();

            case TXT:
                return getPlainTextFilter();

            case HTML:
            case XHTML:
            case HTM:
                return getHtmlFilter();

            case ODP:
            case ODS:
            case ODT:
                return getOpenOfficeFilter();

            case PHP: // TODO add to Matecat
                return getPhpFilter();

            case PROPERTIES:
                return getPropertiesFilter();

            case PO:
                return getPoFilter();

            case XLF:
            case XLIFF:
                return getXliffFilter();

            case JSON: // TODO add to Matecat
                return getJsonFilter();

            case IDML:
                return getIdmlFilter();

            case TXML: // TODO add to Matecat
                return getTxmlFilter();

            case YML: // TODO add to Matecat
                return getYmlFilter();

            case RKM: // TODO add to Matecat
                return getRkmFilter();

            case MIF:
                return getMifFilter();

            default:
                throw new RuntimeException("There is no filter configured for the format: " + format);
        }
    }

    private static OpenXMLFilter getOpenXMLFilter() {
        OpenXMLFilter filter = new OpenXMLFilter();
        ConditionalParameters conditionalParameters = (ConditionalParameters) filter.getParameters();
        conditionalParameters.setCleanupAggressively(true);
        conditionalParameters.setTranslateWordExcludeGraphicMetaData(true);
        conditionalParameters.setTranslateDocProperties(false);
        conditionalParameters.setTranslateComments(false);
        conditionalParameters.setTranslatePowerpointMasters(false);
        conditionalParameters.setTranslateWordHeadersFooters(true);
        conditionalParameters.setTranslateWordHidden(false);
        filter.setParameters(conditionalParameters);
        return filter;
    }

    private static PlainTextFilter getPlainTextFilter() {
        PlainTextFilter filter = new PlainTextFilter();
        // net.sf.okapi.filters.plaintext.Parameters params = (net.sf.okapi.filters.plaintext.Parameters) filter.getParameters();
        // filter.setParameters(params);
        return filter;
    }

    private static HtmlFilter getHtmlFilter() {
        HtmlFilter filter = new HtmlFilter();
        // net.sf.okapi.filters.html.Parameters params = (Parameters) filter.getParameters();
        // filter.setParameters(params);
        return filter;
    }

    private static OpenOfficeFilter getOpenOfficeFilter() {
        OpenOfficeFilter filter = new OpenOfficeFilter();
        // net.sf.okapi.filters.openoffice.Parameters params = (Parameters) filter.getParameters();
        // params.setConvertSpacesTabs(false);
        // params.setExtractNotes(false);
        // params.setExtractReferences(false);
        // filter.setParameters(params);
        return filter;
    }

    private static PHPContentFilter getPhpFilter() {
        PHPContentFilter filter = new PHPContentFilter();
        // net.sf.okapi.filters.php.Parameters params = (net.sf.okapi.filters.php.Parameters) filter.getParameters();
        // filter.setParameters(params);
        return filter;
    }

    private static PropertiesFilter getPropertiesFilter() {
        PropertiesFilter filter = new PropertiesFilter();
        // net.sf.okapi.filters.properties.Parameters params = (net.sf.okapi.filters.properties.Parameters) filter.getParameters();
        // params.setExtraComments();
        // params.setEscapeExtendedChars();
        // params.setCommentsAreNotes();
        // params.setConvertLFandTab();
        // filter.setParameters(params);
        return filter;
    }

    private static POFilter getPoFilter() {
        POFilter filter = new POFilter();
        // net.sf.okapi.filters.po.Parameters params = (net.sf.okapi.filters.po.Parameters) filter.getParameters();
        // params.setAllowEmptyOutputTarget();
        // params.setBilingualMode();
        // params.setWrapContent();
        return filter;
    }

    private static XLIFFFilter getXliffFilter() {
        XLIFFFilter filter = new XLIFFFilter();
        // net.sf.okapi.filters.xliff.Parameters params = (net.sf.okapi.filters.xliff.Parameters) filter.getParameters();
        // params.setAddAltTrans();
        // params.setAddTargetLanguage();
        // params.setAllowEmptyTargets();
        // params.setAlwaysUseSegSource();
        // params.setBalanceCodes();
        // params.setEditAltTrans();
        // params.setEscapeGT();
        // params.setIgnoreInputSegmentation();
        // params.setIncludeExtensions();
        // params.setIncludeIts();
        // params.setOverrideTargetLanguage();
        // params.setAllowEmptyOutputTarget();
        // params.setBilingualMode();
        // params.setWrapContent();
        return filter;
    }

    private static JSONFilter getJsonFilter() {
        JSONFilter filter = new JSONFilter();
        // net.sf.okapi.filters.json.Parameters params = (net.sf.okapi.filters.json.Parameters) filter.getParameters();
        // params.setExtractAllPairs();
        // params.setExtractStandalone();
        // params.setUseFullKeyPath();
        // params.setUseKeyAsName();
        return filter;
    }

    private static IDMLFilter getIdmlFilter() {
        IDMLFilter filter = new IDMLFilter();
        // net.sf.okapi.filters.idml.Parameters params = (net.sf.okapi.filters.idml.Parameters) filter.getParameters();
        // params.setExtractHiddenLayers();
        // params.setExtractMasterSpreads();
        // params.setExtractNotes();
        // params.setNewTuOnBr();
        // params.setSimplifyCodes();
        return filter;
    }

    private static TXMLFilter getTxmlFilter() {
        TXMLFilter filter = new TXMLFilter();
        // net.sf.okapi.filters.txml.Parameters params = (net.sf.okapi.filters.txml.Parameters) filter.getParameters();
        // params.setAllowEmptyOutputTarget();
        // filter.setParameters(params);
        return filter;
    }

    private static RailsYamlFilter getYmlFilter() {
        RailsYamlFilter filter = new RailsYamlFilter();
        // net.sf.okapi.filters.railsyaml.Parameters params = (net.sf.okapi.filters.railsyaml.Parameters) filter.getParameters();
        // params.setEscapeNonAscii();
        // filter.setParameters(params);
        return filter;
    }

    private static RainbowKitFilter getRkmFilter() {
        RainbowKitFilter filter = new RainbowKitFilter();
        // net.sf.okapi.filters.rainbowkit.Parameters params = (net.sf.okapi.filters.rainbowkit.Parameters) filter.getParameters();
        // filter.setParameters(params);
        return filter;
    }

    private static MIFFilter getMifFilter() {
        MIFFilter filter = new MIFFilter();
        // net.sf.okapi.filters.mif.Parameters params = (net.sf.okapi.filters.mif.Parameters) filter.getParameters();
        // params.setExtractBodyPages();
        // params.setExtractHiddenPages();
        // params.setExtractIndexMarkers();
        // params.setExtractLinks();
        // params.setExtractMasterPages();
        // params.setExtractReferencePages();
        // params.setExtractVariables();
        // filter.setParameters(params);
        return filter;
    }






}
