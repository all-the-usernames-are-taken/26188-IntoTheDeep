package org.firstinspires.ftc.mcqueencode;

import org.firstinspires.ftc.vision.opencv.ColorRange;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.opencv.core.RotatedRect;

public class WebcamUtils {

    // This one is a special trick I picked up from the RoadRunner code.
    // Instance variables (that is, normal variables within a class) don't
    // stick around between one OpMode and the next.  But *static* variables
    // are different; they belong not to an instance of a class, but to the
    // class itself, and (in indirect consequence) it turns out they stick
    // around even between OpModes.  
    //
    // So inside your Autonomous code, remember when we
    // set that Boolean "redAlliance"?  Let's change that up.
    //
    // In your Autonomous code, delete the initial declaration of
    // the boolean variable "redAlliance" (or "allianceRed", I forget).
    // Also add "import org.firstinspires.ftc.teamcode.WebcamUtils" to that code.
    //
    // Then use Find-Replace (Ctrl-F) to turn all remaining references
    // to "redAlliance" into references to "WebcamUtils.lookForRed" instead.
    // 
    // The difference?  If you do it this way, then in your *TeleOp* code,
    // you can also import WebcamUtils and reference the *same* boolean variable
    // there.  It'll be set to the same value as it was in your Autonomous,
    // even though otherwise (for your purposes) they're two totally distinct
    // programs.
    public static boolean lookForRed = true;
    
    // (Mental note to Sean & MJ, as they're building that BaseOpMode... the
    // same trick would be an excellent way to keep track of whether your arm
    // encoder needs to be reset to zero at the start of the OpMode or not.
    // Just give BaseOpMode a public static boolean called something like
    // "armEncoderHasBeenZeroed" that starts out false, and then in your init,
    // if it's false, zero the encoder & set this boolean to true.)
    
    // =======================
    
    // These next three functions are all slightly-complex code which is more finicky than I figure you guys
    // would prefer to deal with, so I'm setting them up for you to help you out.  These relate to the code
    // inside the ConceptVisionColorLocator sample code, so go there and work with it before reading further.
    
    // All of the results of "boxFit" can be distorted by the way OpenCV handles RotatedRectangles.
    // For our purposes, we're gonna assert that the "actual" rectangle will always be wider than it is tall,
    // and when that's not true, we're gonna fix the results such that by convention we see angles from -90 to 0,
    // and width > height again.
    
    // See https://namkeenman.wordpress.com/2015/12/18/open-cv-determine-angle-of-rotatedrect-minarearect/
    // for more discussion on this, although I found that his results weren't *quite* the behaviour we get here -
    // he was getting angles from -90 to -0, whereas I'm seeing angles from -0 to +90.  Same basic idea still applies.
    
    // This changes three of our values.  And while there *are* ways to return three new values from a single
    // function, they're not worth getting into here.  So I've just written you three functions, one for each.
    
    public static double getAngle(RotatedRect rect) 
    {
        if (rect.size.width > rect.size.height) return rect.angle;
        else return rect.angle - 90.0;
    }
    
    public static int getWidth(RotatedRect rect)
    {
        org.opencv.core.Size size = rect.size;
        if (size.width > size.height) return (int)size.width;
        else return (int)size.height;
    }
    
    public static int getHeight(RotatedRect rect) 
    {
        org.opencv.core.Size size = rect.size;
        if (size.width > size.height) return (int)size.height;
        else return (int)size.width;
    }
}
