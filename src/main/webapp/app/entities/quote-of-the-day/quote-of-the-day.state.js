(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('quote-of-the-day', {
            parent: 'entity',
            url: '/quote-of-the-day?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'QuoteOfTheDays'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/quote-of-the-day/quote-of-the-days.html',
                    controller: 'QuoteOfTheDayController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('quote-of-the-day-detail', {
            parent: 'quote-of-the-day',
            url: '/quote-of-the-day/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'QuoteOfTheDay'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/quote-of-the-day/quote-of-the-day-detail.html',
                    controller: 'QuoteOfTheDayDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'QuoteOfTheDay', function($stateParams, QuoteOfTheDay) {
                    return QuoteOfTheDay.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'quote-of-the-day',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('quote-of-the-day-detail.edit', {
            parent: 'quote-of-the-day-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/quote-of-the-day/quote-of-the-day-dialog.html',
                    controller: 'QuoteOfTheDayDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['QuoteOfTheDay', function(QuoteOfTheDay) {
                            return QuoteOfTheDay.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('quote-of-the-day.new', {
            parent: 'quote-of-the-day',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/quote-of-the-day/quote-of-the-day-dialog.html',
                    controller: 'QuoteOfTheDayDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                quoteText: null,
                                author: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('quote-of-the-day', null, { reload: 'quote-of-the-day' });
                }, function() {
                    $state.go('quote-of-the-day');
                });
            }]
        })
        .state('quote-of-the-day.edit', {
            parent: 'quote-of-the-day',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/quote-of-the-day/quote-of-the-day-dialog.html',
                    controller: 'QuoteOfTheDayDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['QuoteOfTheDay', function(QuoteOfTheDay) {
                            return QuoteOfTheDay.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('quote-of-the-day', null, { reload: 'quote-of-the-day' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('quote-of-the-day.delete', {
            parent: 'quote-of-the-day',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/quote-of-the-day/quote-of-the-day-delete-dialog.html',
                    controller: 'QuoteOfTheDayDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['QuoteOfTheDay', function(QuoteOfTheDay) {
                            return QuoteOfTheDay.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('quote-of-the-day', null, { reload: 'quote-of-the-day' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
