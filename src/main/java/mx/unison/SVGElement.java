package mx.unison;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public abstract class SVGElement {
    protected Color strokeColor = Color.BLACK;
    protected Color fillColor = null;
    protected float strokeWidth = 1.0f;

    public void setStroke(String strokeAttr) {
        if (strokeAttr != null && !strokeAttr.isEmpty() && !strokeAttr.equals("none")) {
            this.strokeColor = parseColor(strokeAttr);
        }
    }

    public void setFill(String fillAttr) {
        if (fillAttr != null && !fillAttr.isEmpty() && !fillAttr.equals("none")) {
            this.fillColor = parseColor(fillAttr);
        }
    }

    public void setStrokeWidth(String widthAttr) {
        if (widthAttr != null && !widthAttr.isEmpty()) {
            try {
                this.strokeWidth = Float.parseFloat(widthAttr);
            } catch (NumberFormatException e) {
                this.strokeWidth = 1.0f;
            }
        }
    }

    protected Color parseColor(String colorStr) {
        colorStr = colorStr.trim().toLowerCase();
        switch (colorStr) {
            case "red": return Color.RED;
            case "blue": return Color.BLUE;
            case "green": return Color.GREEN;
            case "yellow": return Color.YELLOW;
            case "black": return Color.BLACK;
            case "white": return Color.WHITE;
            case "orange": return Color.ORANGE;
            case "navy": return new Color(0, 0, 128);
            case "lightblue": return new Color(173, 216, 230);
            case "lightgreen": return new Color(144, 238, 144);
            default:
                if (colorStr.startsWith("#")) {
                    try {
                        return Color.decode(colorStr);
                    } catch (NumberFormatException e) {
                        return Color.BLACK;
                    }
                }
                return Color.BLACK;
        }
    }

    public abstract void draw(Graphics2D g2d);
}

class LineElement extends SVGElement {
    private double x1, y1, x2, y2;

    public LineElement(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (strokeColor != null) {
            g2d.setColor(strokeColor);
            g2d.setStroke(new BasicStroke(strokeWidth));
            g2d.draw(new Line2D.Double(x1, y1, x2, y2));
        }
    }
}

class CircleElement extends SVGElement {
    private double cx, cy, r;

    public CircleElement(double cx, double cy, double r) {
        this.cx = cx;
        this.cy = cy;
        this.r = r;
    }

    @Override
    public void draw(Graphics2D g2d) {
        double x = cx - r;
        double y = cy - r;
        double diameter = r * 2;
        Ellipse2D circle = new Ellipse2D.Double(x, y, diameter, diameter);

        if (fillColor != null) {
            g2d.setColor(fillColor);
            g2d.fill(circle);
        }
        if (strokeColor != null) {
            g2d.setColor(strokeColor);
            g2d.setStroke(new BasicStroke(strokeWidth));
            g2d.draw(circle);
        }
    }
}

class RectElement extends SVGElement {
    private double x, y, width, height;

    public RectElement(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Graphics2D g2d) {
        Rectangle2D rect = new Rectangle2D.Double(x, y, width, height);

        if (fillColor != null) {
            g2d.setColor(fillColor);
            g2d.fill(rect);
        }
        if (strokeColor != null) {
            g2d.setColor(strokeColor);
            g2d.setStroke(new BasicStroke(strokeWidth));
            g2d.draw(rect);
        }
    }
}

class EllipseElement extends SVGElement {
    private double cx, cy, rx, ry;

    public EllipseElement(double cx, double cy, double rx, double ry) {
        this.cx = cx;
        this.cy = cy;
        this.rx = rx;
        this.ry = ry;
    }

    @Override
    public void draw(Graphics2D g2d) {
        double x = cx - rx;
        double y = cy - ry;
        double width = rx * 2;
        double height = ry * 2;
        Ellipse2D ellipse = new Ellipse2D.Double(x, y, width, height);

        if (fillColor != null) {
            g2d.setColor(fillColor);
            g2d.fill(ellipse);
        }
        if (strokeColor != null) {
            g2d.setColor(strokeColor);
            g2d.setStroke(new BasicStroke(strokeWidth));
            g2d.draw(ellipse);
        }
    }
}

class PolylineElement extends SVGElement {
    private double[] xPoints;
    private double[] yPoints;

    public PolylineElement(String pointsStr) {
        parsePoints(pointsStr);
    }

    private void parsePoints(String pointsStr) {
        if (pointsStr == null || pointsStr.isEmpty()) {
            xPoints = new double[0];
            yPoints = new double[0];
            return;
        }

        String[] tokens = pointsStr.trim().split("[,\\s]+");
        int n = tokens.length / 2;
        xPoints = new double[n];
        yPoints = new double[n];

        for (int i = 0; i < n; i++) {
            try {
                xPoints[i] = Double.parseDouble(tokens[2 * i]);
                yPoints[i] = Double.parseDouble(tokens[2 * i + 1]);
            } catch (NumberFormatException e) {
                xPoints[i] = 0;
                yPoints[i] = 0;
            }
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (xPoints.length < 2) return;

        Path2D path = new Path2D.Double();
        path.moveTo(xPoints[0], yPoints[0]);
        for (int i = 1; i < xPoints.length; i++) {
            path.lineTo(xPoints[i], yPoints[i]);
        }

        if (fillColor != null) {
            path.closePath();
            g2d.setColor(fillColor);
            g2d.fill(path);
        }

        if (strokeColor != null) {
            g2d.setColor(strokeColor);
            g2d.setStroke(new BasicStroke(strokeWidth));
            g2d.draw(path);
        }
    }
}

class TextElement extends SVGElement {
    private double x, y;
    private String textContent;
    private String fontFamily;
    private int fontSize;

    public TextElement(double x, double y, String textContent, String fontFamily, String fontSizeStr) {
        this.x = x;
        this.y = y;
        this.textContent = textContent != null ? textContent : "";
        this.fontFamily = (fontFamily != null && !fontFamily.isEmpty()) ? fontFamily : "SansSerif";
        
        int size = 12;
        if (fontSizeStr != null && !fontSizeStr.isEmpty()) {
            try {
                size = Integer.parseInt(fontSizeStr.replaceAll("[^0-9]", ""));
            } catch (NumberFormatException e) {
                size = 12;
            }
        }
        this.fontSize = size;
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (strokeColor != null) {
            g2d.setColor(strokeColor);
            g2d.setFont(new Font(fontFamily, Font.PLAIN, fontSize));
            g2d.drawString(textContent, (float) x, (float) y);
        }
    }
}