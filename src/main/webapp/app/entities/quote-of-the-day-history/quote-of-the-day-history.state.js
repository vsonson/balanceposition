(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('quote-of-the-day-history', {
            parent: 'entity',
            url: '/quote-of-the-day-history?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'QuoteOfTheDayHistories'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/quote-of-the-day-history/quote-of-the-day-histories.html',
                    controller: 'QuoteOfTheDayHistoryController',
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
        .state('quote-of-the-day-history-detail', {
            parent: 'quote-of-the-day-history',
            url: '/quote-of-the-day-history/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'QuoteOfTheDayHistory'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/quote-of-the-day-history/quote-of-the-day-history-detail.html',
                    controller: 'QuoteOfTheDayHistoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'QuoteOfTheDayHistory', function($stateParams, QuoteOfTheDayHistory) {
                    return QuoteOfTheDayHistory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'quote-of-the-day-history',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('quote-of-the-day-history-detail.edit', {
            parent: 'quote-of-the-day-history-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/quote-of-the-day-history/quote-of-the-day-history-dialog.html',
                    controller: 'QuoteOfTheDayHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['QuoteOfTheDayHistory', function(QuoteOfTheDayHistory) {
                            return QuoteOfTheDayHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('quote-of-the-day-history.new', {
            parent: 'quote-of-the-day-history',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/quote-of-the-day-history/quote-of-the-day-history-dialog.html',
                    controller: 'QuoteOfTheDayHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('quote-of-the-day-history', null, { reload: 'quote-of-the-day-history' });
                }, function() {
                    $state.go('quote-of-the-day-history');
                });
            }]
        })
        .state('quote-of-the-day-history.edit', {
            parent: 'quote-of-the-day-history',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/quote-of-the-day-history/quote-of-the-day-history-dialog.html',
                    controller: 'QuoteOfTheDayHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['QuoteOfTheDayHistory', function(QuoteOfTheDayHistory) {
                            return QuoteOfTheDayHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('quote-of-the-day-history', null, { reload: 'quote-of-the-day-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('quote-of-the-day-history.delete', {
            parent: 'quote-of-the-day-history',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/quote-of-the-day-history/quote-of-the-day-history-delete-dialog.html',
                    controller: 'QuoteOfTheDayHistoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['QuoteOfTheDayHistory', function(QuoteOfTheDayHistory) {
                            return QuoteOfTheDayHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('quote-of-the-day-history', null, { reload: 'quote-of-the-day-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
