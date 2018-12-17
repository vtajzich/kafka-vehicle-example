package com.kafka.vehicle.domain

import spock.lang.Specification
import spock.lang.Unroll

class PositionTest extends Specification {

    @Unroll
    def "should calculate distance between two positions"() {
        given:
        Position position1 = Position.of(pos1x, pos1y)
        Position position2 = Position.of(pos2x, pos2y)

        when:
        double distance = position1.distance(position2)


        then:
        distance.round(1) == expectedDistance

        where:
        pos1x | pos1y | pos2x | pos2y || expectedDistance
        12    | 11    | 35    | 5     || 23.8
        12    | 11    | 53    | -11   || 46.5

    }

}
