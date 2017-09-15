(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('track-metric', {
            parent: 'entity',
            url: '/track-metric?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TrackMetrics'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/track-metric/track-metrics.html',
                    controller: 'TrackMetricController',
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
        .state('track-metric-detail', {
            parent: 'track-metric',
            url: '/track-metric/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TrackMetric'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/track-metric/track-metric-detail.html',
                    controller: 'TrackMetricDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'TrackMetric', function($stateParams, TrackMetric) {
                    return TrackMetric.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'track-metric',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('track-metric-detail.edit', {
            parent: 'track-metric-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/track-metric/track-metric-dialog.html',
                    controller: 'TrackMetricDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TrackMetric', function(TrackMetric) {
                            return TrackMetric.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('track-metric.new', {
            parent: 'track-metric',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/track-metric/track-metric-dialog.html',
                    controller: 'TrackMetricDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                trackIcon: null,
                                infoBubble: null,
                                infoBubbleContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('track-metric', null, { reload: 'track-metric' });
                }, function() {
                    $state.go('track-metric');
                });
            }]
        })
        .state('track-metric.edit', {
            parent: 'track-metric',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/track-metric/track-metric-dialog.html',
                    controller: 'TrackMetricDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TrackMetric', function(TrackMetric) {
                            return TrackMetric.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('track-metric', null, { reload: 'track-metric' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('track-metric.delete', {
            parent: 'track-metric',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/track-metric/track-metric-delete-dialog.html',
                    controller: 'TrackMetricDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TrackMetric', function(TrackMetric) {
                            return TrackMetric.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('track-metric', null, { reload: 'track-metric' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
