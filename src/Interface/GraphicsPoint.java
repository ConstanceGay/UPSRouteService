package Interface;

public class GraphicsPoint {

    private int row;
    private int col;

    GraphicsPoint() {
        this.row = 0;
        this.col = 0;
    }

    GraphicsPoint(int col, int row) {
        this.col = col;
        this.row = row;
    }

    double getDistanceFrom(GraphicsPoint graphicsPoint) {
        return Math.sqrt(Math.pow(graphicsPoint.row - row, 2) + Math.pow(graphicsPoint.col - col, 2));
    }

    void setRow(int row) {
        this.row = row;
    }

    void setCol(int col) {
        this.col = col;
    }

    int getRow() {
        return row;
    }

    int getCol() {
        return col;
    }

    public String toString() {
        return "[col: " + col + ", row: " + row + "]";
    }

}
