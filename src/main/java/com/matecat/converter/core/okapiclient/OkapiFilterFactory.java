package com.matecat.converter.core.okapiclient;

import com.matecat.converter.core.format.Format;
import com.matecat.converter.core.okapiclient.customfilters.CustomFiltersRouter;
import net.sf.okapi.common.filters.FilterConfigurationMapper;
import net.sf.okapi.common.filters.IFilter;
import net.sf.okapi.common.filters.IFilterConfigurationMapper;
import net.sf.okapi.filters.archive.ArchiveFilter;
import net.sf.okapi.filters.dtd.DTDFilter;
import net.sf.okapi.filters.html.HtmlFilter;
import net.sf.okapi.filters.icml.ICMLFilter;
import net.sf.okapi.filters.idml.IDMLFilter;
import net.sf.okapi.filters.json.JSONFilter;
import net.sf.okapi.filters.mif.MIFFilter;
import net.sf.okapi.filters.openoffice.OpenOfficeFilter;
import net.sf.okapi.filters.openxml.OpenXMLFilter;
import net.sf.okapi.filters.php.PHPContentFilter;
import net.sf.okapi.filters.plaintext.PlainTextFilter;
import net.sf.okapi.filters.po.POFilter;
import net.sf.okapi.filters.properties.PropertiesFilter;
import net.sf.okapi.filters.table.tsv.TabSeparatedValuesFilter;
import net.sf.okapi.filters.xml.XMLFilter;
import net.sf.okapi.filters.yaml.YamlFilter;
import net.sf.okapi.filters.rainbowkit.RainbowKitFilter;
import net.sf.okapi.filters.regex.RegexFilter;
import net.sf.okapi.filters.table.csv.CommaSeparatedValuesFilter;
import net.sf.okapi.filters.ts.TsFilter;
import net.sf.okapi.filters.ttx.TTXFilter;
import net.sf.okapi.filters.txml.TXMLFilter;
import net.sf.okapi.filters.xini.XINIFilter;
import net.sf.okapi.filters.xliff.XLIFFFilter;
import net.sf.okapi.filters.xmlstream.XmlStreamFilter;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Filter factory
 */
    public class OkapiFilterFactory {

    // Path of the configurations
    public static final String OKAPI_CUSTOM_CONFIGS_PATH = "/okapi/configurations/";

    private static final String XML_CONFIG_FILENAME = "okf_xmlstream-custom.fprm";
    private static final String HTML_CONFIG_FILENAME = "okf_html-custom.fprm";
    private static final String DITA_CONFIG_FILENAME = "okf_xmlstream@dita-custom.fprm";

    private static final CustomFiltersRouter customFiltersRouter = new CustomFiltersRouter();

    // Formats supported by the filter factory
    protected static final Set<Format> SUPPORTED_FORMATS;
    static {
        SUPPORTED_FORMATS = new HashSet<>(Arrays.asList(
                Format.DOCX,
                Format.PPTX,
                Format.XLSX,
                Format.TXT,
                Format.HTML,
                Format.XHTML,
                Format.HTM,
                Format.ODP,
                Format.OTP,
                Format.ODS,
                Format.OTS,
                Format.ODT,
                Format.OTT,
                Format.PHP,
                Format.PROPERTIES,
                Format.PO,
                Format.SDLXLIFF,
                Format.XLF,
                Format.XLIFF,
                Format.JSON,
                Format.YAML,
                Format.IDML,
                Format.ICML,
                Format.TXML,
                Format.YAML,
                Format.RKM,
                Format.MIF,
                Format.XML,
                Format.DITA,
                Format.CSV,
                Format.XINI,
                Format.TTX,
                Format.TS,
                Format.PO,
                Format.STRINGS,
                Format.ARCHIVE,
                Format.DTD,
                Format.RESX,
                Format.SRT,
                Format.TSV,
                Format.WIX
        ));
    }


    /**
     * Check if Okapi supports a format
     * @param format Format
     * @return True if it's supported, false otherwise
     */
    protected static boolean isSupported(Format format) {
        return SUPPORTED_FORMATS.contains(format);
    }


    /**
     * Get the corresponding filter for a given format
     * @return
     */
    protected static IFilter getFilter(File file) {
        IFilter filter = customFiltersRouter.getFilter(file);
        if (filter != null) return filter;

        final Format format = Format.getFormat(file);
        switch (format) {
            case DOCX:
            case PPTX:
            case XLSX:      return getOpenXMLFilter();
            case HTML:
            case XHTML:
            case HTM:       return getHtmlFilter();
            case ODP:
            case OTP:
            case ODS:
            case OTS:
            case ODT:
            case OTT:       return getOpenOfficeFilter();
            case SDLXLIFF:
            case XLF:
            case XLIFF:     return getXliffFilter();
            case TXT:       return getPlainTextFilter();
            case PHP:       return getPhpFilter();
            case PROPERTIES:return getPropertiesFilter();
            case PO:        return getPoFilter();
            case JSON:      return getJsonFilter();
            case IDML:      return getIdmlFilter();
            case ICML:      return getIcmlFilter();
            case TXML:      return getTxmlFilter();
            case YAML:      return getYmlFilter();
            case RKM:       return getRkmFilter();
            case MIF:       return getMifFilter();
            case XML:       return getXmlFilter();
            case DITA:      return getDitaFilter();
            case CSV:       return getCSVFilter();
            case ARCHIVE:   return getArchiveFilter();
            case XINI:      return getXiniFilter();
            case TTX:       return getTTXFilter();
            case TS:        return getTSFilter();
            case STRINGS:   return getStringsFilter();
            case RESX:      return getRESXFilter();
            case WIX:       return getWixFilter();
            case TSV:       return getTSVFilter();
            case SRT:       return getSRTFilter();
            case DTD:       return getDTDFilter();
            default: throw new RuntimeException("There is no filter configured for the format: " + format);
        }
    }


    /*
     * FILTERS
     */

    private static OpenXMLFilter getOpenXMLFilter() {
        OpenXMLFilter filter = new OpenXMLFilter();
        net.sf.okapi.filters.openxml.ConditionalParameters conditionalParameters = ( net.sf.okapi.filters.openxml.ConditionalParameters) filter.getParameters();
        // Global
        conditionalParameters.setCleanupAggressively(true);
        conditionalParameters.setTranslateDocProperties(false);
        conditionalParameters.setTranslateComments(false);
        conditionalParameters.setAddTabAsCharacter(true);
        conditionalParameters.setAddLineSeparatorCharacter(true);
        // Word specific
        conditionalParameters.setTranslateWordExcludeGraphicMetaData(true);
        conditionalParameters.setTranslateWordHeadersFooters(true);
        conditionalParameters.setTranslateWordHidden(false);
        // Excel specific
        conditionalParameters.setTranslateExcelHidden(false);
        // Powerpoint specific
        conditionalParameters.setTranslatePowerpointMasters(false);
        return filter;
    }

    private static PlainTextFilter getPlainTextFilter() {
        PlainTextFilter filter = new PlainTextFilter();
        // net.sf.okapi.filters.plaintext.Parameters params = (net.sf.okapi.filters.plaintext.Parameters) filter.getParameters();
        return filter;
    }

    private static DTDFilter getDTDFilter() {
        DTDFilter filter = new DTDFilter();
        return filter;
    }

    private static HtmlFilter getHtmlFilter() {
        HtmlFilter filter = new HtmlFilter();
        try {
            net.sf.okapi.filters.html.Parameters params = (net.sf.okapi.filters.html.Parameters) filter.getParameters();
            String config = IOUtils.toString(System.class.getResourceAsStream(OKAPI_CUSTOM_CONFIGS_PATH + HTML_CONFIG_FILENAME), "UTF-8");
            params.fromString(config);
        } catch (IOException e) {
            System.err.println("Dita custom configuration could not be loaded");
        }
        return filter;
    }

    private static OpenOfficeFilter getOpenOfficeFilter() {
        OpenOfficeFilter filter = new OpenOfficeFilter();
        // net.sf.okapi.filters.openoffice.Parameters params = (Parameters) filter.getParameters();
        // params.setConvertSpacesTabs(false);
        // params.setExtractNotes(false);
        // params.setExtractReferences(false);
        return filter;
    }

    private static PHPContentFilter getPhpFilter() {
        PHPContentFilter filter = new PHPContentFilter();
        // net.sf.okapi.filters.php.Parameters params = (net.sf.okapi.filters.php.Parameters) filter.getParameters();
        return filter;
    }

    private static PropertiesFilter getPropertiesFilter() {
        PropertiesFilter filter = new PropertiesFilter();
        // net.sf.okapi.filters.properties.Parameters params = (net.sf.okapi.filters.properties.Parameters) filter.getParameters();
        // params.setExtraComments();
        // params.setEscapeExtendedChars();
        // params.setCommentsAreNotes();
        // params.setConvertLFandTab();
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

    private static TsFilter getTSFilter() {
        TsFilter filter = new TsFilter();
        // net.sf.okapi.filters.ts.Parameters params = (net.sf.okapi.filters.ts.Parameters) filter.getParameters();
        // params.setEscapeGT();
        // params.setDecodeByteValues();
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

    private static ICMLFilter getIcmlFilter() {
        ICMLFilter filter = new ICMLFilter();
        return filter;
    }

    private static TXMLFilter getTxmlFilter() {
        TXMLFilter filter = new TXMLFilter();
        // net.sf.okapi.filters.txml.Parameters params = (net.sf.okapi.filters.txml.Parameters) filter.getParameters();
        // params.setAllowEmptyOutputTarget();
        return filter;
    }

    private static YamlFilter getYmlFilter() {
        YamlFilter filter = new YamlFilter();
        // net.sf.okapi.filters.railsyaml.Parameters params = (net.sf.okapi.filters.railsyaml.Parameters) filter.getParameters();
        // params.setEscapeNonAscii();
        return filter;
    }

    private static RainbowKitFilter getRkmFilter() {
        return new RainbowKitFilter();
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
        return filter;
    }

    private static CommaSeparatedValuesFilter getCSVFilter() {
        return new CommaSeparatedValuesFilter();
    }

    private static TabSeparatedValuesFilter getTSVFilter() {
        return new TabSeparatedValuesFilter();
    }

    private static ArchiveFilter getArchiveFilter() {
        return new ArchiveFilter();
    }

    private static XINIFilter getXiniFilter() {
        XINIFilter filter = new XINIFilter();
        //net.sf.okapi.filters.xini.Parameters params = (net.sf.okapi.filters.xini.Parameters) filter.getParameters();
        //params.setUseOkapiSegmentation();
        return filter;
    }

    private static TTXFilter getTTXFilter() {
        TTXFilter filter = new TTXFilter();
        //net.sf.okapi.filters.ttx.Parameters params = (net.sf.okapi.filters.ttx.Parameters) filter.getParameters();
        //params.setEscapeGT();
        return filter;
    }

    private static RegexFilter getStringsFilter() {
        RegexFilter filter = new RegexFilter();
        try {
            net.sf.okapi.filters.regex.Parameters params = (net.sf.okapi.filters.regex.Parameters) filter.getParameters();
            String config = IOUtils.toString(System.class.getResourceAsStream(OKAPI_CUSTOM_CONFIGS_PATH + "okf_regex@macstrings.fprm"), "UTF-8");
            params.fromString(config);
        } catch (IOException e) {
            System.err.println("Strings custom configuration could not be loaded");
        }
        return filter;
    }

    private static RegexFilter getSRTFilter() {
        RegexFilter filter = new RegexFilter();
        try {
            net.sf.okapi.filters.regex.Parameters params = (net.sf.okapi.filters.regex.Parameters) filter.getParameters();
            String config = IOUtils.toString(System.class.getResourceAsStream(OKAPI_CUSTOM_CONFIGS_PATH + "okf_regex@srt.fprm"), "UTF-8");
            params.fromString(config);
        } catch (IOException e) {
            System.err.println("Strings custom configuration could not be loaded");
        }
        return filter;
    }

    private static XMLFilter getRESXFilter() {
        XMLFilter filter = new XMLFilter();
        try {
            net.sf.okapi.filters.its.Parameters params = (net.sf.okapi.filters.its.Parameters) filter.getParameters();
            String config = IOUtils.toString(System.class.getResourceAsStream(OKAPI_CUSTOM_CONFIGS_PATH + "okf_xml@resx.fprm"), "UTF-8");
            params.fromString(config);
        } catch (IOException e) {
            System.err.println("XML custom configuration could not be loaded");
        }
        return filter;
    }

    private static XMLFilter getWixFilter() {
        XMLFilter filter = new XMLFilter();
        try {
            net.sf.okapi.filters.its.Parameters params = (net.sf.okapi.filters.its.Parameters) filter.getParameters();
            String config = IOUtils.toString(System.class.getResourceAsStream(OKAPI_CUSTOM_CONFIGS_PATH + "okf_xml@wix.fprm"), "UTF-8");
            params.fromString(config);
        } catch (IOException e) {
            System.err.println("XML custom configuration could not be loaded");
        }
        return filter;
    }

    private static XmlStreamFilter getXmlFilter() {
        XmlStreamFilter filter = new XmlStreamFilter();
        try {
            net.sf.okapi.filters.xmlstream.Parameters params = (net.sf.okapi.filters.xmlstream.Parameters) filter.getParameters();
            String config = IOUtils.toString(System.class.getResourceAsStream(OKAPI_CUSTOM_CONFIGS_PATH + XML_CONFIG_FILENAME), "UTF-8");
            params.fromString(config);
        } catch (IOException e) {
            System.err.println("XML custom configuration could not be loaded");
        }
        IFilterConfigurationMapper cm = new FilterConfigurationMapper();
        cm.addConfigurations(HtmlFilter.class.getName());
        filter.setFilterConfigurationMapper(cm);
        return filter;
    }

    private static XmlStreamFilter getDitaFilter() {
        XmlStreamFilter filter = new XmlStreamFilter();
        try {
            net.sf.okapi.filters.xmlstream.Parameters params = (net.sf.okapi.filters.xmlstream.Parameters) filter.getParameters();
            String config = IOUtils.toString(System.class.getResourceAsStream(OKAPI_CUSTOM_CONFIGS_PATH + DITA_CONFIG_FILENAME), "UTF-8");
            params.fromString(config);
        } catch (IOException e) {
            System.err.println("Dita custom configuration could not be loaded");
        }
        return filter;
    }

}
