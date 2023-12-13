package fr.ignfab.geotools;

import java.io.InputStream;

import java.net.URL;
import java.net.URLConnection;

import org.geotools.wfs.GML;
import org.geotools.wfs.GML.Version;
import org.geotools.referencing.CRS;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;

/**
 * Hello world!
 *
 */
public class App 
{
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
        InputStream rawDataStream = urlConnection.getInputStream();
            
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
        System.out.println("THIS IS THE END OF TIMES\n");
    }

}
