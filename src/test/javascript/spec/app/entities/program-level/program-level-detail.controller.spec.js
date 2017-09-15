'use strict';

describe('Controller Tests', function() {

    describe('ProgramLevel Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockProgramLevel, MockProgramStep, MockProgram, MockProgramHistory;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockProgramLevel = jasmine.createSpy('MockProgramLevel');
            MockProgramStep = jasmine.createSpy('MockProgramStep');
            MockProgram = jasmine.createSpy('MockProgram');
            MockProgramHistory = jasmine.createSpy('MockProgramHistory');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ProgramLevel': MockProgramLevel,
                'ProgramStep': MockProgramStep,
                'Program': MockProgram,
                'ProgramHistory': MockProgramHistory
            };
            createController = function() {
                $injector.get('$controller')("ProgramLevelDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'balancepositionApp:programLevelUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
