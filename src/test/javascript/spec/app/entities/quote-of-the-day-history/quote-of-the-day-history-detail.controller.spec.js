'use strict';

describe('Controller Tests', function() {

    describe('QuoteOfTheDayHistory Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockQuoteOfTheDayHistory, MockUserInfo, MockQuoteOfTheDay;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockQuoteOfTheDayHistory = jasmine.createSpy('MockQuoteOfTheDayHistory');
            MockUserInfo = jasmine.createSpy('MockUserInfo');
            MockQuoteOfTheDay = jasmine.createSpy('MockQuoteOfTheDay');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'QuoteOfTheDayHistory': MockQuoteOfTheDayHistory,
                'UserInfo': MockUserInfo,
                'QuoteOfTheDay': MockQuoteOfTheDay
            };
            createController = function() {
                $injector.get('$controller')("QuoteOfTheDayHistoryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'balancepositionApp:quoteOfTheDayHistoryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
