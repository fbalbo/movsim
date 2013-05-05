/*
 * Copyright (C) 2010, 2011, 2012 by Arne Kesting, Martin Treiber, Ralph Germ, Martin Budden
 * <movsim.org@gmail.com>
 * -----------------------------------------------------------------------------------------
 * 
 * This file is part of
 * 
 * MovSim - the multi-model open-source vehicular-traffic simulator.
 * 
 * MovSim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * MovSim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MovSim. If not, see <http://www.gnu.org/licenses/>
 * or <http://www.movsim.org>.
 * 
 * -----------------------------------------------------------------------------------------
 */
package org.movsim.simulator.trafficlights;

import java.util.HashSet;
import java.util.Set;

import org.movsim.autogen.TrafficLightStatus;
import org.movsim.simulator.roadnetwork.RoadSegment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * The Class TrafficLight.
 */
public class TrafficLight {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(TrafficLight.class);

    /** The status. */
    private TrafficLightStatus status;

    private double position = Double.NaN;

    private final String type; // not unique in network

    private final String groupId; // unique mapping to infrastructure

    private TriggerCallback triggerCallback;

    private final Set<TrafficLightStatus> possibleStati = new HashSet<>();

    private RoadSegment roadSegment;

    public TrafficLight(String type, String groupId) {
        this.type = type;
        this.groupId = groupId;
    }

    /**
     * Returns the type of the trafficlight as referenced in the movsim input.
     * 
     * <p>
     * Note that this 'logical' trafficlight type does not reference a unique signal in the infrastructure.
     * 
     * @return type
     */
    public String type() {
        return type;
    }

    /**
     * Returns the controllergroup-id which allows for a unique reference to a set of signals in the infrastructure.
     * 
     * @return groupId
     */
    public String groupId() {
        return groupId;
    }

    public TrafficLightStatus status() {
        return status;
    }

    void setState(TrafficLightStatus newStatus) {
        this.status = newStatus;
    }

    public double position() {
        Preconditions.checkArgument(!Double.isNaN(position), "traffic light without position");
        return position;
    }

    public boolean hasPosition() {
        return !Double.isNaN(position);
    }

    public void setPosition(double position) {
        Preconditions.checkArgument(Double.isNaN(this.position), "trafficlight of type=\"" + type()
                + "\" position already set: " + toString());
        this.position = position;
    }

    public void triggerNextPhase() {
        triggerCallback.nextPhase();
    }

    void addPossibleState(TrafficLightStatus status) {
        possibleStati.add(status);
    }

    /**
     * Return the number of lights this traffic light has, can be 1, 2 or 3.
     * 
     * @return
     */
    public int lightCount() {
        return Math.min(3, possibleStati.size());
    }

    public RoadSegment roadSegment() {
        return Preconditions.checkNotNull(roadSegment);
    }

    public void setRoadSegment(RoadSegment roadSegment) {
        Preconditions.checkArgument(this.roadSegment == null, "roadSegment already set");
        this.roadSegment = roadSegment;
    }

    @Override
    public String toString() {
        return "TrafficLight [status=" + status + ", position=" + position + ", type=" + type + ", groupId = "
                + groupId + ", roadSegment.id=" + ((roadSegment == null) ? "null" : roadSegment.userId()) + "]";
    }

    void setTriggerCallback(TriggerCallback triggerCallback) {
        this.triggerCallback = Preconditions.checkNotNull(triggerCallback);
    }

}
