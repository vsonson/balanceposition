'use strict';

describe('Controller Tests', function() {

    describe('TrackMetricQuestion Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTrackMetricQuestion, MockTrackMetric, MockKeyPair;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTrackMetricQuestion = jasmine.createSpy('MockTrackMetricQuestion');
            MockTrackMetric = jasmine.createSpy('MockTrackMetric');
            MockKeyPair = jasmine.createSpy('MockKeyPair');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'TrackMetricQuestion': MockTrackMetricQuestion,
                'TrackMetric': MockTrackMetric,
                'KeyPair': MockKeyPair
            };
            createController = function() {
                $injector.get('$controller')("TrackMetricQuestionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'balancepositionApp:trackMetricQuestionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
