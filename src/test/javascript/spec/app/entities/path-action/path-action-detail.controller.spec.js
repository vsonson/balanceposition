'use strict';

describe('Controller Tests', function() {

    describe('PathAction Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPathAction, MockTrackMetric, MockProgram, MockPathStep;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPathAction = jasmine.createSpy('MockPathAction');
            MockTrackMetric = jasmine.createSpy('MockTrackMetric');
            MockProgram = jasmine.createSpy('MockProgram');
            MockPathStep = jasmine.createSpy('MockPathStep');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'PathAction': MockPathAction,
                'TrackMetric': MockTrackMetric,
                'Program': MockProgram,
                'PathStep': MockPathStep
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
