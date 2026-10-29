package mx.unison;

public class Main {

    public static void main(String[] args) {
        String filePath = "xml/test_svg_dom.svg";

        if (args.length > 0) {
            filePath = args[0];
        }

        SVGParser parser = new SVGParser();
        parser.parseSVG(filePath);
    }
}