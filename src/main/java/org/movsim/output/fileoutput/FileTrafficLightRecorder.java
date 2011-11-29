/**
 * Copyright (C) 2010, 2011 by Arne Kesting, Martin Treiber, Ralph Germ, Martin Budden <info@movsim.org>
 * ----------------------------------------------------------------------
 * 
 * This file is part of
 * 
 * MovSim - the multi-model open-source vehicular-traffic simulator
 * 
 * MovSim is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * MovSim is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with MovSim. If not, see <http://www.gnu.org/licenses/> or
 * <http://www.movsim.org>.
 * 
 * ----------------------------------------------------------------------
 */
package org.movsim.output.fileoutput;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import org.movsim.input.ProjectMetaData;
import org.movsim.simulator.MovsimConstants;
import org.movsim.simulator.roadnetwork.TrafficLight;
import org.movsim.utilities.impl.FileUtils;

/**
 * The Class FileTrafficLightRecorder.
 */
public class FileTrafficLightRecorder {

    private PrintWriter fstr = null;
    private final int nDt;

    /**
     * Instantiates a new traffic light recorder.
     * 
     * @param nDt
     *            the n dt
     * @param trafficLights
     *            the traffic lights
     */
    public FileTrafficLightRecorder(int nDt, List<TrafficLight> trafficLights) {

        this.nDt = nDt;

        // road id hard coded as 1 for the moment
        final ProjectMetaData projectMetaData = ProjectMetaData.getInstance();
        final String outputPath = projectMetaData.getOutputPath();
        final String filename = outputPath + File.separator + projectMetaData.getProjectName() + ".id%d_tl_log.csv";
        fstr = FileUtils.getWriter(filename);
        writeHeader(trafficLights);

    }

    /**
     * Update.
     * 
     * @param iterationCount
     *            the itime
     * @param time
     *            the time
     * @param trafficLights
     *            the traffic lights
     */
    public void update(long iterationCount, double time, List<TrafficLight> trafficLights) {

        if (iterationCount % nDt != 0) {
            // no update; nothing to do
            return;
        }

        // write data:
        if (fstr != null) {
            fstr.printf("%8.2f   ", time);
            for (final TrafficLight trafficLight : trafficLights) {
                fstr.printf("%.1f  %d  ", trafficLight.position(), trafficLight.status());
            }
            fstr.printf("%n");
            fstr.flush();
        }
    }

    /**
     * Write header.
     * 
     * @param trafficLights
     *            the traffic lights
     */
    private void writeHeader(List<TrafficLight> trafficLights) {
        // write header:
        fstr.printf(MovsimConstants.COMMENT_CHAR + " number codes for traffic lights status: %n");
        fstr.printf(MovsimConstants.COMMENT_CHAR + " green         %d %n", TrafficLight.GREEN_LIGHT);
        fstr.printf(MovsimConstants.COMMENT_CHAR + " green --> red %d %n", TrafficLight.GREEN_RED_LIGHT);
        fstr.printf(MovsimConstants.COMMENT_CHAR + " red           %d %n", TrafficLight.RED_LIGHT);
        fstr.printf(MovsimConstants.COMMENT_CHAR + " red --> green %d %n", TrafficLight.RED_GREEN_LIGHT);

        int counter = 1;
        for (final TrafficLight trafficLight : trafficLights) {
            fstr.printf(MovsimConstants.COMMENT_CHAR + " position of traffic light no. %d: %5.2f m%n", counter,
                    trafficLight.position());
            counter++;
        }
        fstr.printf(MovsimConstants.COMMENT_CHAR + " %-8s  %-8s  %-8s  %-8s %n", "time[s]", "position[m]_TL1",
                "status[1]_TL1", " etc. ");
        fstr.flush();
    }

}
