package pl.G0bi74.WebServer2;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class RectangleController {
    private List<Rectangle> rectangles = new ArrayList<Rectangle>();

    @GetMapping("/rect")
    public String init() {
        rectangles.add(new Rectangle(10, 20, 70, 40, "Black"));
        rectangles.add(new Rectangle(80, 10, 7, 4, "green"));
        rectangles.add(new Rectangle(0, 2, 40, 70, "yellow"));
        rectangles.add(new Rectangle(100, 100, 100, 100, "red"));
        return "<h2>Initialized</h2>";
    }


    @PostMapping("/addRectangle")
    public String addRectangle(@RequestBody Rectangle rectangle) {
        rectangles.add(rectangle);
        return "<h2>Added</h2>";
    }
    @GetMapping("/getRectangleAtIndex")
    public Rectangle getRectangleAtIndex(@RequestParam int index) {
        return rectangles.get(index);
    }
    @PutMapping("/modAtIndex")
    public String modRectangleAtIndex(@RequestParam int index, @RequestBody Rectangle rectangle) {
        if(rectangles.size()-1 < index) return "<h2>Error index out of bounds</h2>";
        else{
            rectangles.set(index, rectangle);
            return "<h2>Updated</h2>";
        }
    }
    @DeleteMapping("/deleteAtIndex")
    public String deleteRectangleAtIndex(@RequestParam int index) {
        if(rectangles.size()-1 < index) return "<h2>Error index out of bounds</h2>";
        else{
            rectangles.remove(index);
            return "<h2>Deleted</h2>";
        }
    }

    @GetMapping("/getRectangle")
    public Rectangle getRectangle() {
        return rectangles.getFirst();
    }
    @GetMapping("/list")
    public List<Rectangle> listRectangles() {
        return rectangles;
    }

    @GetMapping("/svg")
    public String toSVG() {
        StringBuilder sb = new StringBuilder();
        sb.append("<svg width=\"600\" height=\"600\" xmlns=\"http://www.w3.org/2000/svg\">\n");

        for (Rectangle r : rectangles) {
            sb.append("<rect width=\""+r.width+"\" height=\""+r.height+"\" x=\""+r.x+"\" y=\""+r.y+"\" fill=\""+r.color+"\" />\n");
        }
        sb.append(" </svg>");
        return sb.toString();
    }
}
