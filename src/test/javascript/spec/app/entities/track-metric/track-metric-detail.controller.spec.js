'use strict';

describe('Controller Tests', function() {

    describe('TrackMetric Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTrackMetric, MockTrackMetricQuestion;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTrackMetric = jasmine.createSpy('MockTrackMetric');
            MockTrackMetricQuestion = jasmine.createSpy('MockTrackMetricQuestion');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'TrackMetric': MockTrackMetric,
                'TrackMetricQuestion': MockTrackMetricQuestion
            };
            createController = function() {
                $injector.get('$controller')("TrackMetricDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'balancepositionApp:trackMetricUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
