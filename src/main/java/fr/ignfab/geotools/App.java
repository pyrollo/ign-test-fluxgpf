package fr.ignfab.geotools;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;

import java.net.URL;
import java.net.URLConnection;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import org.geotools.wfs.GML;
import org.geotools.wfs.GML.Version;
import org.geotools.referencing.CRS;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;

//import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.FeatureSource;

import org.apache.poi.util.ReplacingInputStream;

public class App 
{

    public static void wfsng() throws Exception
    {

        Iterator availableStores =  DataStoreFinder.getAvailableDataStores();
        System.out.println("List available Stores:");
        while (availableStores.hasNext()) {
            System.out.println("   " + availableStores.next().toString());
        }

        // See https://docs.geotools.org/latest/userguide/library/data/wfs-ng.html

        String getCapabilities = "https://data.geopf.fr/wfs/wfs?SERVICE=WFS&VERSION=2.0.0&REQUEST=GetCapabilities";

        Map connectionParameters = new HashMap();
        connectionParameters.put("WFSDataStoreFactory:GET_CAPABILITIES_URL", getCapabilities );

        DataStore data = DataStoreFinder.getDataStore( connectionParameters );
        /* Display type names
        String typeNames[] = data.getTypeNames();
        for (String t: typeNames) {           
            System.out.println(t); 
        }
        */
        FeatureSource<SimpleFeatureType, SimpleFeature> source = data.getFeatureSource("BDTOPO_V3:cimetiere");
        System.out.println( "Metadata Bounds: " + source.getBounds() );

        /*
        String geomName = schema.getDefaultGeometry().getLocalName();
        Envelope bbox = new Envelope( -100.0, -70, 25, 40 );
               BDTOPO_V3:cimetiere
               */
    }

    public static void fromURL() throws Exception
    {
		CoordinateReferenceSystem wgs84Crs = CRS.decode("EPSG:4326");

//        String url = "https://wxs.ign.fr/topographie/geoportail/wfs?SERVICE=WFS&VERSION=1.1.0"+
        String url = "https://data.geopf.fr/wfs/ows?SERVICE=WFS&VERSION=2.0.0"+
            "&TYPENAME=BDTOPO_V3:cimetiere&REQUEST=GetFeature"+
            "&SRSNAME=EPSG:2154&BBOX=650556.55754621070,6859402.33482602000,651068.55754621070,6859914.33482602000,EPSG:2154"+
            "&STARTINDEX=0&MAXFEATURES=1000";

//        url = "https://data.geopf.fr/wfs/wfs?SERVICE=WFS&REQUEST=GetFeature&VERSION=2.0.0&TYPENAMES=BDTOPO_V3:cimetiere&STARTINDEX=0&COUNT=1000000&SRSNAME=urn:ogc:def:crs:EPSG::4326&BBOX=48.71180421753307144,2.17865471308054914,48.96896620898196772,2.44789335494747862,urn:ogc:def:crs:EPSG::4326";

/*        String url = "https://data.geopf.fr/wfs/ows?SERVICE=WFS&VERSION=2.0.0"+
            "&TYPENAME=BDTOPO_V3:cimetiere&REQUEST=GetFeature"+
            "&SRSNAME=EPSG:2154&BBOX=650556.55754621070,6859402.33482602000,651068.55754621070,6859914.33482602000,EPSG:2154"+
            "&STARTINDEX=0&MAXFEATURES=1000";
*/
        System.out.println("URL=" + url);

        /* get the stream */
        URL urlGetFeature = new URL(url);
        URLConnection urlConnection = urlGetFeature.openConnection();
//        InputStream rawDataStream = urlConnection.getInputStream();
        InputStream rawDataStream = new ReplacingInputStream(
            new ReplacingInputStream(
                urlConnection.getInputStream(),
                "wfs:member", "gml:featureMembers"),
            "http://BDTOPO_V3".getBytes("UTF-8"), "http://BDTOPOV3".getBytes("UTF-8"));
/*
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        int b;
        while (-1 != (b = rawDataStream.read()))
            bos.write(b);

        System.out.println(new String(bos.toByteArray()));
*/

        //treat the stream
        GML gml = new GML(Version.GML3);

		gml.setCoordinateReferenceSystem(wgs84Crs);

		SimpleFeatureCollection features = gml.decodeFeatureCollection(rawDataStream);

        if(features != null) {
            System.out.println("Number of features : " + String.valueOf(features.size()));

            SimpleFeatureIterator it = features.features();
            while (it.hasNext()) {
                System.out.println("NEXT FEATURE");
                SimpleFeature currentFeature = it.next();
            }
        } else {
            System.out.println("FEATURES NULL");
        }
  
	}

    public static void fromFile() throws Exception
    {
		CoordinateReferenceSystem wgs84Crs = CRS.decode("EPSG:4326");

        /* get the stream */
        InputStream rawDataStream = new FileInputStream(new File("wfs.xml"));
            
        //treat the stream
        GML gml = new GML(Version.GML3);

		gml.setCoordinateReferenceSystem(wgs84Crs);

		SimpleFeatureCollection features = gml.decodeFeatureCollection(rawDataStream);

        if(features != null) {
            System.out.println("Number of features : " + String.valueOf(features.size()));

            SimpleFeatureIterator it = features.features();
            while (it.hasNext()) {
                System.out.println("NEXT FEATURE");
                SimpleFeature currentFeature = it.next();
            }
        } else {
            System.out.println("FEATURES NULL");
        }
	}

    public static void main( String[] args ) throws Exception
    {
        System.out.println("\nHERE IS THE STARTING POINT OF EVERYTHING" );
        App.fromURL();
        //App.fromFile();
        //App.wfsng();
        System.out.println("THIS IS THE END OF TIMES\n");
    }

}
