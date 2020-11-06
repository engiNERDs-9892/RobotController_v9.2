package org.firstinspires.ftc.teamcode.skills;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.autonomous.AutoRoute;
import org.firstinspires.ftc.teamcode.tfrec.Detector;
import org.firstinspires.ftc.teamcode.tfrec.classification.Classifier;
import org.firstinspires.ftc.teamcode.autonomous.AutoDot;

import java.util.ArrayList;
import java.util.List;

public class RingDetector {
    Telemetry telemetry;
    private Detector tfDetector = null;
    private HardwareMap hardwareMap;
    private Led lights;

    private static String MODEL_FILE_NAME = "rings_float.tflite";
    private static String LABEL_FILE_NAME = "labels.txt";
    private static Classifier.Model MODEl_TYPE = Classifier.Model.FLOAT_EFFICIENTNET;
    private static final String LABEL_A = "None";
    private static final String LABEL_B = "Single";
    private static final String LABEL_C = "Quad";
    private String targetZone = LABEL_C;

    public RingDetector(HardwareMap hMap, Led led, Telemetry t) {
        hardwareMap = hMap;
        telemetry = t;
        lights = led;
        initDetector();
        activateDetector();
    }

    public AutoDot detectRing(int timeout, String side, Telemetry telemetry, LinearOpMode caller) {
        AutoDot zone = new AutoDot();
        zone.setX(70);
        zone.setY(130);
        zone.setHeading(0);
        boolean found = false;
        boolean stop = false;

        ElapsedTime runtime = new ElapsedTime();
        runtime.reset();
        while (!stop || runtime.seconds() <= timeout) {
            if (tfDetector != null) {
                List<Classifier.Recognition> results = tfDetector.getLastResults();
                if (results == null || results.size() == 0) {
                    telemetry.addData("Info", "No results");
                } else {
                    for (Classifier.Recognition r : results) {
                        if (r.getConfidence() >= 0.8) {
                            telemetry.addData("PrintZone", r.getTitle());
                            if (r.getTitle().contains(LABEL_C)) {
                                if (side.equals(AutoRoute.NAME_RED)) {
                                    zone.setX(70);
                                    zone.setY(120);
                                    zone.setHeading(45);
                                } else {
                                    zone.setX(30);
                                    zone.setY(12);
                                    zone.setHeading(45);
                                }
                                found = true;
                                targetZone = LABEL_C;
                                this.lights.recognitionSignal(4);
                            }
                            if (r.getTitle().contains(LABEL_B)) {
                                if (side.equals(AutoRoute.NAME_RED)) {
                                    zone.setX(50);
                                    zone.setY(90);
                                    zone.setHeading(45);
                                } else {
                                    zone.setX(30);
                                    zone.setY(12);
                                    zone.setHeading(45);
                                }
                                found = true;
                                targetZone = LABEL_B;
                                this.lights.recognitionSignal(1);
                            }
                            if (r.getTitle().contains(LABEL_A)) {
                                if (side.equals(AutoRoute.NAME_RED)) {
                                    zone.setX(78);
                                    zone.setY(70);
                                    zone.setHeading(0);
                                } else {
                                    zone.setX(30);
                                    zone.setY(12);
                                    zone.setHeading(45);
                                }
                                found = true;
                                targetZone = LABEL_A;
                                this.lights.recognitionSignal(0);
                            }
                        }
                    }
                }
                telemetry.update();
            }
            if (found || !caller.opModeIsActive()) {
                stop = true;
            }
        }

        return zone;
    }

    public void initDetector() {
        tfDetector = new Detector(MODEl_TYPE, MODEL_FILE_NAME, LABEL_FILE_NAME, hardwareMap.appContext, telemetry);
    }

    protected void activateDetector() {
        if (tfDetector != null) {
            tfDetector.activate();
        }
        telemetry.addData("Info", "TF Activated");
    }

    public String returnZone() {
        return targetZone;
    }

    public void stopDetection() {
        if (tfDetector != null) {
            tfDetector.stopProcessing();
        }
    }
}
