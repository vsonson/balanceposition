'use strict';

describe('Controller Tests', function() {

    describe('MetricHistory Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMetricHistory, MockTrackMetric, MockTrackMetricQuestion, MockUserInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMetricHistory = jasmine.createSpy('MockMetricHistory');
            MockTrackMetric = jasmine.createSpy('MockTrackMetric');
            MockTrackMetricQuestion = jasmine.createSpy('MockTrackMetricQuestion');
            MockUserInfo = jasmine.createSpy('MockUserInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'MetricHistory': MockMetricHistory,
                'TrackMetric': MockTrackMetric,
                'TrackMetricQuestion': MockTrackMetricQuestion,
                'UserInfo': MockUserInfo
            };
            createController = function() {
                $injector.get('$controller')("MetricHistoryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'balancepositionApp:metricHistoryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
