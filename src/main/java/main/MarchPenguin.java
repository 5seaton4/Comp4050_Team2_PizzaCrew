package main;
/* autogenerated by Processing revision 1283 on 2022-09-01 */
import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;

public class MarchPenguin extends PApplet {

  /* A march of the Penguins animation
     Created for Comp4050
     2022
     Alice's submission
  */

  static Penguin[] colony = new Penguin[7];
  Landscape southPole = new Landscape(500);

  public void setup() {
    /* size commented out by preprocessor */ ;

    for (int i = 0; i < colony.length; i++) {
      colony[i] = new Penguin(random(0, width), 400, random(0.5f, 1.5f));
    }
  }

  public void draw() {
    background(200, 0, 100);
    southPole.drawHills();
    for (int i = 0; i < colony.length; i++) {
      colony[i].display();
      colony[i].walk(southPole, colony);
    }
    southPole.drawGround();
  }

  public void mousePressed() {
    for (int i = 0; i < colony.length; i++) {
      if (colony[i].select(mouseX, mouseY)) {
        break;
      }
    }
  }

  public void mouseReleased() {
    for (int i = 0; i < colony.length; i++) {
      colony[i].deSelect();
    }
  }

  public void mouseDragged() {
    for (int i = 0; i < colony.length; i++) {
      colony[i].drag(mouseX, mouseY);
    }
  }

  class Beak {

    float x, y, size;
    int angle = 0;
    int angularSpeed = 3;
    int squeakCounter = 0;

    Beak(float x, float y, float size) {
      this.x = x;
      this.y = y;
      this.size = size;
    }

    public void display() {
      fill(200, 150, 100);
      stroke(210, 160, 120);
      pushMatrix();
      translate(x, y);
      rotate(radians(angle));
      triangle(0, 0, 0, size * 0.45f, size, 0);
      rotate(radians(-2 * angle));
      triangle(0, 0, 0, -size * 0.45f, size, 0);
      popMatrix();
    }

    public void move() {
      if (squeakCounter > 0) {
        if (angle <= 0 && angularSpeed < 0) {
          angularSpeed = -angularSpeed;
          squeakCounter--;
        }
        if (angle >= 45 && angularSpeed > 0) {
          angularSpeed = -angularSpeed;
        }
        angle += angularSpeed;
      }
    }

    public void squeak() {
      squeakCounter = 3;
    }
  }

  class Landscape {

    float yOff;

    Landscape(float y) {
      yOff = y;
    }

    public void drawHills() {
      fill(222); // We are going to draw a polygon out of the wave points
      noStroke();
      beginShape();

      // Iterate over horizontal pixels
      for (float x = 0; x <= width; x += 10) {
        float y = hillLevel(x);
        // Set the vertex

        vertex(x, y);
      }
      vertex(width, height);
      vertex(0, height);
      endShape(CLOSE);
    }

    public void drawGround() {
      fill(255); // We are going to draw a polygon out of the wave points
      noStroke();
      beginShape();

      // Iterate over horizontal pixels
      for (float x = 0; x <= width; x += 10) {
        float y = groundLevel(x);
        // Set the vertex
        vertex(x, y);
      }
      vertex(width, height);
      vertex(0, height);
      endShape(CLOSE);
    }

    public float hillLevel(float x) {
      return map(noise(x * 0.02f), 0, 1, yOff - height / 3, yOff - height / 2);
    }

    public float groundLevel(float x) {
      return map(noise(x * 0.005f), 0, 1, yOff - 20, yOff + 20);
    }
  }

  class Penguin {
    float x, y, dx;
    float target_velocity = 0;
    int SIZE = 50;
    boolean isSelected = false;

    Beak bill;

    // Constructor to set the initial position, and the intial and target velocity.
    Penguin(float x, float y, float dx) {
      this.x = x;
      this.y = y - 2 * SIZE;
      this.target_velocity = dx;
      this.dx = dx;
      this.bill = new Beak(SIZE * 0.45f, 0, SIZE / 2);
    }

    public void display() {
      pushMatrix();
      translate(this.x, this.y);
      // Black part
      fill(0);
      stroke(1);
      strokeWeight(2);
      ellipse(0, 0, SIZE, SIZE);
      ellipse(0, SIZE, SIZE, 2 * SIZE);
      rect(-SIZE / 2, SIZE, SIZE, SIZE);
      triangle(0, 0.75f * SIZE, 0, 2 * SIZE, -SIZE, 2 * SIZE);

      // The beak
      bill.display();

      // The white part
      fill(255);
      noStroke();
      arc(0, 0, SIZE, SIZE, -QUARTER_PI, QUARTER_PI, OPEN);
      arc(SIZE * 1.44f, 0, 3 * SIZE, SIZE, 3 * QUARTER_PI, 5 * QUARTER_PI, OPEN);
      arc(0, SIZE, SIZE, 2 * SIZE, -QUARTER_PI, QUARTER_PI);

      // The eyes
      fill(100, 100, 200);
      ellipse(SIZE / 4, -SIZE / 5, SIZE / 6, SIZE / 6);
      popMatrix();
    }

    public void walk(Landscape land, Penguin[] peers) {
      if (!isSelected) {
        this.x += this.dx;
        this.y = land.groundLevel(this.x) - 2 * SIZE;
        if (this.x >= width + 2 * SIZE) {
          this.x = -2 * SIZE;
        }
        if (isClose(peers)) {
          this.dx = this.dx * 0.95f;
        } else {
          this.dx = (9 * this.dx + this.target_velocity) / 10;
        }
      }
      this.bill.move();
    }

    public boolean select(float x, float y) {
      this.isSelected = isOver(x, y);
      return this.isSelected;
    }

    public void deSelect() {
      this.isSelected = false;
    }

    public void drag(float x, float y) {
      if (this.isSelected) {
        this.x = x;
        this.y = y;
        this.bill.squeak();
      }
    }

    public boolean isOver(float x, float y) {
      boolean result = dist(x, y, this.x, this.y) <= SIZE / 2;
      result =
          result
              || ((x >= this.x - SIZE / 2)
                  && (x <= this.x + SIZE / 2)
                  && (y >= this.y)
                  && (this.y <= this.y + 2 * SIZE));
      return result;
    }

    public boolean isClose(Penguin[] peers) {
      boolean result = false;
      for (int i = 0; i < peers.length; i++) {
        if (this != peers[i]
            && dist(peers[i].x, peers[i].y, this.x, this.y) <= 2 * SIZE
            && peers[i].x > this.x) {
          result = true;
        }
      }
      return result;
    }
  }

  public void settings() {
    size(1000, 600);
  }

  public static void main(String[] passedArgs) {
    String[] appletArgs = new String[] {"MarchPenguin"};
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
