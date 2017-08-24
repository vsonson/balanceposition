(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('incentive-history', {
            parent: 'entity',
            url: '/incentive-history?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'IncentiveHistories'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/incentive-history/incentive-histories.html',
                    controller: 'IncentiveHistoryController',
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
                }]
            }
        })
        .state('incentive-history-detail', {
            parent: 'incentive-history',
            url: '/incentive-history/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'IncentiveHistory'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/incentive-history/incentive-history-detail.html',
                    controller: 'IncentiveHistoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'IncentiveHistory', function($stateParams, IncentiveHistory) {
                    return IncentiveHistory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'incentive-history',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('incentive-history-detail.edit', {
            parent: 'incentive-history-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/incentive-history/incentive-history-dialog.html',
                    controller: 'IncentiveHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['IncentiveHistory', function(IncentiveHistory) {
                            return IncentiveHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('incentive-history.new', {
            parent: 'incentive-history',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/incentive-history/incentive-history-dialog.html',
                    controller: 'IncentiveHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                points: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('incentive-history', null, { reload: 'incentive-history' });
                }, function() {
                    $state.go('incentive-history');
                });
            }]
        })
        .state('incentive-history.edit', {
            parent: 'incentive-history',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/incentive-history/incentive-history-dialog.html',
                    controller: 'IncentiveHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['IncentiveHistory', function(IncentiveHistory) {
                            return IncentiveHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('incentive-history', null, { reload: 'incentive-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('incentive-history.delete', {
            parent: 'incentive-history',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/incentive-history/incentive-history-delete-dialog.html',
                    controller: 'IncentiveHistoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['IncentiveHistory', function(IncentiveHistory) {
                            return IncentiveHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('incentive-history', null, { reload: 'incentive-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
