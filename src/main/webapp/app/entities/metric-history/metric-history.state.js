(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('metric-history', {
            parent: 'entity',
            url: '/metric-history?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MetricHistories'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/metric-history/metric-histories.html',
                    controller: 'MetricHistoryController',
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
        .state('metric-history-detail', {
            parent: 'metric-history',
            url: '/metric-history/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MetricHistory'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/metric-history/metric-history-detail.html',
                    controller: 'MetricHistoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MetricHistory', function($stateParams, MetricHistory) {
                    return MetricHistory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'metric-history',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('metric-history-detail.edit', {
            parent: 'metric-history-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/metric-history/metric-history-dialog.html',
                    controller: 'MetricHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MetricHistory', function(MetricHistory) {
                            return MetricHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('metric-history.new', {
            parent: 'metric-history',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/metric-history/metric-history-dialog.html',
                    controller: 'MetricHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                metricValue: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('metric-history', null, { reload: 'metric-history' });
                }, function() {
                    $state.go('metric-history');
                });
            }]
        })
        .state('metric-history.edit', {
            parent: 'metric-history',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/metric-history/metric-history-dialog.html',
                    controller: 'MetricHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MetricHistory', function(MetricHistory) {
                            return MetricHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('metric-history', null, { reload: 'metric-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('metric-history.delete', {
            parent: 'metric-history',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/metric-history/metric-history-delete-dialog.html',
                    controller: 'MetricHistoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MetricHistory', function(MetricHistory) {
                            return MetricHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('metric-history', null, { reload: 'metric-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
