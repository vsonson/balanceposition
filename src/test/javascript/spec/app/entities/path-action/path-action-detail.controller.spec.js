'use strict';

describe('Controller Tests', function() {

    describe('PathAction Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPathAction, MockPathStep, MockTrackMetric, MockProgram;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPathAction = jasmine.createSpy('MockPathAction');
            MockPathStep = jasmine.createSpy('MockPathStep');
            MockTrackMetric = jasmine.createSpy('MockTrackMetric');
            MockProgram = jasmine.createSpy('MockProgram');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'PathAction': MockPathAction,
                'PathStep': MockPathStep,
                'TrackMetric': MockTrackMetric,
                'Program': MockProgram
            };
            createController = function() {
                $injector.get('$controller')("PathActionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'balancepositionApp:pathActionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
