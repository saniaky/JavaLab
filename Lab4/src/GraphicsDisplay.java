import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class GraphicsDisplay extends JPanel {
    private Double[][] graphicsData;

    private int selectedMarker = -1;

    private boolean showAxis = true;
    private boolean showMarkers = true;
    private boolean showGrid = true;
    private boolean setRotate = false;

    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    private ArrayList<double[][]> undoHistory = new ArrayList(5);
    private double[][] viewport = new double[2][2];

    private double scaleX;
    private double scaleY;

    private BasicStroke graphicsStroke;
    private BasicStroke gridStroke;
    private BasicStroke axisStroke;
    private BasicStroke markerStroke;
    private BasicStroke selectionStroke;

    private Font axisFont;
    private Font labelsFont;
    private static DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();


    private boolean scaleMode = false;
    private boolean changeMode = false;
    private double[] originalPoint = new double[2];
    private Rectangle2D.Double selectionRect = new Rectangle2D.Double();


    public GraphicsDisplay() {
        setBackground(Color.WHITE);
        graphicsStroke = new BasicStroke(2.0F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0F, new float[]{24, 6, 6, 6, 12, 6, 6, 6, 24, 6}, 0.0F);
        gridStroke = new BasicStroke(1.0F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0F, new float[]{4, 4}, 0.0F);
        axisStroke = new BasicStroke(2.0F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0F, null, 0.0F);
        markerStroke = new BasicStroke(1.0F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0F, null, 0.0F);
        selectionStroke = new BasicStroke(1.0F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0F, new float[]{10.0F, 10.0F}, 0.0F);
        axisFont = new Font("Serif", Font.BOLD, 20);
        labelsFont = new Font("Serif", Font.PLAIN, 10);
        formatter.setMaximumFractionDigits(1);

        addMouseListener(new MouseHandler());
        addMouseMotionListener(new MouseMotionHandler());
    }

    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
        repaint();
    }

    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
        repaint();
    }

    public void setSetRotate(boolean setRotate) {
        this.setRotate = setRotate;
        repaint();
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        repaint();
    }

    public void showGraphics(Double[][] graphicsData) {
        this.graphicsData = graphicsData;

        minX = graphicsData[0][0];
        maxX = graphicsData[graphicsData.length - 1][0];
        minY = graphicsData[0][1];
        maxY = minY;

        for (int i = 1; i < graphicsData.length; i++) {
            if (graphicsData[i][1] < minY) {
                minY = graphicsData[i][1];
            }
            if (graphicsData[i][1] > maxY) {
                maxY = graphicsData[i][1];
            }
        }

        zoomToRegion(minX, maxY, maxX, minY);
    }

    public void zoomToRegion(double x1, double y1, double x2, double y2) {
        viewport[0][0] = x1;
        viewport[0][1] = y1;
        viewport[1][0] = x2;
        viewport[1][1] = y2;
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        double Width = (!setRotate) ? getSize().getWidth() : getSize().getHeight();
        double Height = (!setRotate) ? getSize().getHeight() : getSize().getWidth();

        scaleX = (Width / (viewport[1][0] - viewport[0][0]));
        scaleY = (Height / (viewport[0][1] - viewport[1][1]));

        if (graphicsData == null || graphicsData.length == 0) return;
        Graphics2D canvas = (Graphics2D) g;

        if (setRotate) {
            canvas.rotate(-Math.PI / 2);
            canvas.translate(-Width, 0);
        }
        if (showGrid)
            paintGrid(canvas);
        if (showAxis)
            paintAxis(canvas);
        paintGraphics(canvas);
        if (showMarkers)
            paintMarkers(canvas);
        paintLabels(canvas);
        paintSelection(canvas);
    }

    private void paintSelection(Graphics2D canvas) {
        if (!scaleMode) return;
        canvas.setStroke(selectionStroke);
        canvas.setColor(Color.BLACK);
        canvas.draw(selectionRect);
    }

    private void paintGraphics(Graphics2D canvas) {
        canvas.setStroke(graphicsStroke);
        canvas.setColor(Color.RED);

        Double currentX = null;
        Double currentY = null;
        for (Double[] point : graphicsData) {
            if (((point[0] >= viewport[0][0]) && (point[1]) <= viewport[0][1]) &&
                    (point[0] <= viewport[1][0]) && (point[1] >= viewport[1][1])) {
                if ((currentX != null) && (currentY != null)) {
                    canvas.draw(new Line2D.Double(xyToPoint(currentX, currentY), xyToPoint(point[0], point[1])));
                }
                currentX = point[0];
                currentY = point[1];
            }
        }
    }

    private void paintMarkers(Graphics2D canvas) {
        canvas.setStroke(markerStroke);
        canvas.setColor(Color.RED);
        canvas.setPaint(Color.RED);

        for (Double[] point : graphicsData) {

            int result = point[1].intValue();

            while (result != 0) {
                if ((result % 10) % 2 != 0)
                    break;
                result /= 10;
            }
            if (result == 0 && point[1] != 0 && point[1].intValue() != 0) {
                canvas.setColor(Color.CYAN);
            } else
                canvas.setColor(Color.RED);

            GeneralPath graphics = new GeneralPath();
            Point2D.Double center = xyToPoint(point[0], point[1]);

            graphics.moveTo(center.getX() - 5, center.getY());
            graphics.lineTo(center.getX() + 5, center.getY());

            graphics.moveTo(center.getX(), center.getY() - 5);
            graphics.lineTo(center.getX(), center.getY() + 5);

            graphics.moveTo(center.getX() - 5, center.getY() - 2);
            graphics.lineTo(center.getX() - 5, center.getY() + 2);

            graphics.moveTo(center.getX() + 5, center.getY() - 2);
            graphics.lineTo(center.getX() + 5, center.getY() + 2);

            graphics.moveTo(center.getX() - 2, center.getY() - 5);
            graphics.lineTo(center.getX() + 2, center.getY() - 5);

            graphics.moveTo(center.getX() - 2, center.getY() + 5);
            graphics.lineTo(center.getX() + 2, center.getY() + 5);

            canvas.draw(graphics); // Начертить контур маркера
        }
    }

    private void paintLabels(Graphics2D canvas) {
        canvas.setColor(Color.BLACK);
        canvas.setFont(labelsFont);

        FontRenderContext context = canvas.getFontRenderContext();

        double labelXPos;
        double labelYPos;

        if ((viewport[0][0] < 0) && (viewport[1][0] > 0))
            labelXPos = 0;
        else
            labelXPos = viewport[0][0];

        if ((viewport[1][1] < 0) && (viewport[0][1] > 0))
            labelYPos = 0;
        else
            labelYPos = viewport[1][1];

        double pos = viewport[0][0];
        double step = (viewport[1][0] - viewport[0][0]) / 10;
        while (pos < viewport[1][0]) {
            Point2D.Double point = xyToPoint(pos, labelYPos);
            String label = formatter.format(pos);
            Rectangle2D bounds = labelsFont.getStringBounds(label, context);
            canvas.drawString(label, (float) (point.getX() + 5), (float) (point.getY() - bounds.getHeight()));
            pos += step;
        }

        pos = viewport[1][1];
        step = (viewport[0][1] - viewport[1][1]) / 10;
        while (pos < viewport[0][1]) {
            Point2D.Double point = xyToPoint(labelXPos, pos);
            String label = formatter.format(pos);
            Rectangle2D bounds = this.labelsFont.getStringBounds(label, context);
            canvas.drawString(label, (float) (point.getX() + 5), (float) (point.getY() - bounds.getHeight()));
            pos += step;
        }

        if (selectedMarker >= 0) {
            Point2D.Double point = xyToPoint(graphicsData[selectedMarker][0], graphicsData[selectedMarker][1]);
            String label = "X =" + new DecimalFormat("#.##").format(graphicsData[selectedMarker][0]) +
                    ", Y =" + new DecimalFormat("#.##").format(graphicsData[selectedMarker][1]);
            Rectangle2D bounds = labelsFont.getStringBounds(label, context);
            canvas.setColor(Color.BLUE);

            double dx = 0, dy = 0;
            if (point.getX() + bounds.getWidth() + 5 > getSize().getWidth())
                dx = -bounds.getWidth();
            if (point.getY() + bounds.getHeight() > getSize().getHeight())
                dy = -bounds.getHeight();
            if (point.getY() < getSize().getHeight())
                dy = bounds.getHeight();

            canvas.drawString(label, (float) (point.getX() + dx), (float) (point.getY() + dy));
        }
    }

    private void paintGrid(Graphics2D canvas) {
        canvas.setStroke(gridStroke);
        canvas.setColor(Color.LIGHT_GRAY);

        double pos = 0;
        double step = (viewport[1][0] - viewport[0][0]) / 10;
        while (pos > viewport[0][0] && pos < viewport[1][0]) {
            canvas.draw(new Line2D.Double(xyToPoint(pos, viewport[0][1]), xyToPoint(pos, viewport[1][1])));

            canvas.setColor(Color.RED);
            canvas.setStroke(new BasicStroke(1.0F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0F, null, 0.0F));
            for (int i = 0; i < 10; i++, pos -= step / 10) {
                if (i % 5 == 0)
                    canvas.draw(new Line2D.Double(xyToPoint(pos, -8 / scaleY), xyToPoint(pos, +8 / scaleY)));
                else
                    canvas.draw(new Line2D.Double(xyToPoint(pos, -4 / scaleY), xyToPoint(pos, +4 / scaleY)));

            }
            canvas.setStroke(gridStroke);
            canvas.setColor(Color.LIGHT_GRAY);
            //pos -= step;
        }

        pos = 0;
        while (pos < viewport[1][0] && pos > viewport[0][0]) {
            canvas.draw(new Line2D.Double(xyToPoint(pos, viewport[0][1]), xyToPoint(pos, viewport[1][1])));

            canvas.setColor(Color.RED);
            canvas.setStroke(new BasicStroke(1.0F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0F, null, 0.0F));
            for (int i = 0; i < 10; i++, pos += step / 10) {
                if (i % 5 == 0)
                    canvas.draw(new Line2D.Double(xyToPoint(pos, -8 / scaleY), xyToPoint(pos, +8 / scaleY)));
                else
                    canvas.draw(new Line2D.Double(xyToPoint(pos, -4 / scaleY), xyToPoint(pos, +4 / scaleY)));

            }
            canvas.setStroke(gridStroke);
            canvas.setColor(Color.LIGHT_GRAY);
            //pos += step;
        }
        canvas.draw(new Line2D.Double(xyToPoint(viewport[1][0], viewport[0][1]), xyToPoint(viewport[1][0], viewport[1][1])));

        pos = 0;
        step = (viewport[0][1] - viewport[1][1]) / 10;
        while (pos > viewport[1][1] && pos < viewport[0][1]) {
            canvas.draw(new Line2D.Double(xyToPoint(viewport[0][0], pos), xyToPoint(viewport[1][0], pos)));

            canvas.setColor(Color.RED);
            canvas.setStroke(new BasicStroke(1.0F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0F, null, 0.0F));
            for (int i = 0; i < 10; i++, pos -= step / 10) {
                if (i % 5 == 0)
                    canvas.draw(new Line2D.Double(xyToPoint(-8 / scaleX, pos), xyToPoint(+8 / scaleX, pos)));
                else
                    canvas.draw(new Line2D.Double(xyToPoint(-4 / scaleX, pos), xyToPoint(+4 / scaleX, pos)));

            }
            canvas.setStroke(gridStroke);
            canvas.setColor(Color.LIGHT_GRAY);

            //pos -= step;
        }

        pos = 0;
        while (pos < viewport[0][1] && pos > viewport[1][1]) {
            canvas.draw(new Line2D.Double(xyToPoint(viewport[0][0], pos), xyToPoint(viewport[1][0], pos)));

            canvas.setColor(Color.RED);
            canvas.setStroke(new BasicStroke(1.0F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0F, null, 0.0F));
            for (int i = 0; i < 10; i++, pos += step / 10) {
                if (i % 5 == 0)
                    canvas.draw(new Line2D.Double(xyToPoint(-8 / scaleX, pos), xyToPoint(+8 / scaleX, pos)));
                else
                    canvas.draw(new Line2D.Double(xyToPoint(-4 / scaleX, pos), xyToPoint(+4 / scaleX, pos)));

            }
            canvas.setStroke(gridStroke);
            canvas.setColor(Color.LIGHT_GRAY);

            //pos += step;
        }
        canvas.draw(new Line2D.Double(xyToPoint(viewport[0][0], viewport[0][1]), xyToPoint(viewport[1][0], viewport[0][1])));
    }


    // Метод, обеспечивающий отображение осей координат
    private void paintAxis(Graphics2D canvas) {
        canvas.setStroke(axisStroke);
        canvas.setColor(Color.BLACK);
        canvas.setFont(axisFont);
        FontRenderContext context = canvas.getFontRenderContext();

        if (viewport[0][0] <= 0.0 && viewport[1][0] >= 0.0) {
            canvas.draw(new Line2D.Double(xyToPoint(0, viewport[0][1]), xyToPoint(0, viewport[1][1])));

            GeneralPath arrow = new GeneralPath();
            Point2D.Double lineEnd = xyToPoint(0, viewport[0][1]);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() + 5, arrow.getCurrentPoint().getY() + 20);
            arrow.lineTo(arrow.getCurrentPoint().getX() - 10, arrow.getCurrentPoint().getY());
            arrow.closePath();
            canvas.draw(arrow);
            canvas.fill(arrow);
            Rectangle2D bounds = axisFont.getStringBounds("y", context);
            Point2D.Double labelPos = xyToPoint(0, viewport[0][1]);
            canvas.drawString("y", (float) labelPos.getX() - 20, (float) (labelPos.getY() - bounds.getY()) / 2);
        }

        if (viewport[1][1] <= 0.0 && viewport[0][1] >= 0.0) {
            canvas.draw(new Line2D.Double(xyToPoint(viewport[0][0], 0), xyToPoint(viewport[1][0], 0)));

            GeneralPath arrow = new GeneralPath();
            Point2D.Double lineEnd = xyToPoint(viewport[1][0], 0);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() - 20, arrow.getCurrentPoint().getY() - 5);
            arrow.lineTo(arrow.getCurrentPoint().getX(), arrow.getCurrentPoint().getY() + 10);
            arrow.closePath();
            canvas.draw(arrow);
            canvas.fill(arrow);
            Rectangle2D bounds = axisFont.getStringBounds("x", context);
            Point2D.Double labelPos = xyToPoint(viewport[1][0], 0);
            canvas.drawString("x", (float) (labelPos.getX() - bounds.getWidth() - 10), (float) (labelPos.getY() + bounds.getY()) + 40);
        }
    }


    protected Point2D.Double xyToPoint(double x, double y) {
        double deltaX = x - viewport[0][0];
        double deltaY = viewport[0][1] - y;
        return new Point2D.Double(deltaX * scaleX, deltaY * scaleY);
    }

    protected double[] pointToXY(int x, int y) {
        return new double[]{viewport[0][0] + x / scaleX, viewport[0][1] - y / scaleY};
    }

    protected int findSelectedPoint(int x, int y) {
        if (graphicsData == null) return -1;
        int pos = 0;
        for (Double[] point : graphicsData) {
            Point2D.Double screenPoint = xyToPoint(point[0], point[1]);
            double distance = (screenPoint.getX() - x) * (screenPoint.getX() - x) + (screenPoint.getY() - y) * (screenPoint.getY() - y);
            if (distance < 100) return pos;
            pos++;
        }
        return -1;
    }


    public class MouseHandler extends MouseAdapter {
        public void mouseClicked(MouseEvent ev) {
            if (ev.getButton() == MouseEvent.BUTTON3) {
                if (undoHistory.size() > 0) {
                    viewport = undoHistory.get(undoHistory.size() - 1);
                    undoHistory.remove(undoHistory.size() - 1);
                } else {
                    zoomToRegion(minX, maxY, maxX, minY);
                }
                repaint();
            }
        }

        public void mousePressed(MouseEvent ev) {
            if (ev.getButton() != MouseEvent.BUTTON1) return;
            selectedMarker = findSelectedPoint(getX(ev), getY(ev));
            originalPoint = pointToXY(getX(ev), getY(ev));
            if (selectedMarker >= 0) {
                changeMode = true;
                setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
            } else {
                scaleMode = true;
                setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                selectionRect.setFrame(getX(ev), getY(ev), 1, 1);
            }
        }

        public void mouseReleased(MouseEvent ev) {
            if (ev.getButton() != MouseEvent.BUTTON1) return;
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            if (changeMode) {
                changeMode = false;
            } else {
                scaleMode = false;
                double[] finalPoint = pointToXY(getX(ev), getY(ev));

                if (finalPoint[0] - originalPoint[0] < 0 ||
                        originalPoint[1] - finalPoint[1] < 0) {
                    repaint();
                    return;

                }

                undoHistory.add(viewport);
                viewport = new double[2][2];
                zoomToRegion(originalPoint[0], originalPoint[1], finalPoint[0], finalPoint[1]);
                repaint();
            }
        }
    }

    public class MouseMotionHandler implements MouseMotionListener {

        public void mouseMoved(MouseEvent ev) {
            selectedMarker = findSelectedPoint(getX(ev), getY(ev));
            if (selectedMarker >= 0)
                setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
            else {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
            repaint();
        }

        public void mouseDragged(MouseEvent ev) {
            if (changeMode) {
                double[] currentPoint = pointToXY(getX(ev), getY(ev));
                double newY = graphicsData[selectedMarker][1] + currentPoint[1] - graphicsData[selectedMarker][1];
                if (newY > viewport[0][1]) {
                    newY = viewport[0][1];
                }
                if (newY < viewport[1][1]) {
                    newY = viewport[1][1];
                }
                graphicsData[selectedMarker][1] = newY;
                repaint();
            } else {
                double width = getX(ev) - selectionRect.getX();
                double height = getY(ev) - selectionRect.getY();
                if (width < 5 || height < 5) {
                    return;
                }
                selectionRect.setFrame(selectionRect.getX(), selectionRect.getY(), width, height);
                repaint();
            }
        }
    }

    protected int getX(MouseEvent ev) {
        return (!setRotate) ? ev.getX() : (int) getSize().getHeight() - ev.getY();
    }

    protected int getY(MouseEvent ev) {
        return (!setRotate) ? ev.getY() : ev.getX();
    }
}